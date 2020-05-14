package com.example.pluginproject.hook.loadedapk;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

import java.io.File;
import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;

public class LoadedApkPluginManager {

    private static final LoadedApkPluginManager INSTANCE = new LoadedApkPluginManager();

    private LoadedApkPluginManager(){

    }

    public static LoadedApkPluginManager getInstance(){
        return INSTANCE;
    }

    public void putLoadedApk2Map(Context context, String filePath){
        try {
            //1、拿到系统的ActivityThread对象，找到ActivityThread对象中的 mPackages 这个map集合
            Class activityThreadClass = Class.forName("android.app.ActivityThread");
            Field sCurrentActivityThreadField = activityThreadClass.getDeclaredField("sCurrentActivityThread");
            sCurrentActivityThreadField.setAccessible(true);
            Object sCurrentActivityThread = sCurrentActivityThreadField.get(null);

            Field mPackagesField = activityThreadClass.getDeclaredField("mPackages");
            mPackagesField.setAccessible(true);
            Map mPackages = (Map) mPackagesField.get(sCurrentActivityThread);

            //2、根据插件apk生成对应的LoadedApk对象
            //得到ApplicationInfo对象，这个ApplicationInfo是插件apk的ApplicationInfo
            ApplicationInfo applicationInfo = parseApplicationInfo(filePath);

            //得到CompatibilityInfo对象
            Class compatibilityInfoClass = Class.forName("android.content.res.CompatibilityInfo");
            //CompatibilityInfo 里面有个静态成员变量 DEFAULT_COMPATIBILITY_INFO，是个CompatibilityInfo类型的
            Field defaultCompatibilityInfoField = compatibilityInfoClass.getDeclaredField("DEFAULT_COMPATIBILITY_INFO");
            Object defaultCompatibilityInfo = defaultCompatibilityInfoField.get(null);

            Method getPackageInfoNoCheckMethod = activityThreadClass.getDeclaredMethod("getPackageInfoNoCheck",
                    ApplicationInfo.class, compatibilityInfoClass);

            //根据插件apk的ApplicationInfo和CompatibilityInfo生成LoadedApk
            Object loadedApk = getPackageInfoNoCheckMethod.invoke(sCurrentActivityThread, applicationInfo, defaultCompatibilityInfo);

            //实例化类加载器
            String odexPath = PluginFileUtil.getPluginOptDexPath(context, applicationInfo.packageName).getAbsolutePath();
            String libPath = PluginFileUtil.getPluginLibPath(context, applicationInfo.packageName).getAbsolutePath();
            ClassLoader pluginClassLoader = new PluginClassLoader(filePath, odexPath, libPath, context.getClassLoader());

            //找到LoadedApk对象里面的mClassLoader加载器，然后用我们自己的加载器进行替换
            Field mClassLoaderField = loadedApk.getClass().getDeclaredField("mClassLoader");
            mClassLoaderField.setAccessible(true);
            mClassLoaderField.set(loadedApk, pluginClassLoader);

            //3.将插件的LoadedApk对象添加到mPackages集合中
            mPackages.put(applicationInfo.packageName, new WeakReference<>(loadedApk));

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 生成插件的ApplicationInfo 对象
     *  PackageParser中有个generateApplicationInfo()方法可以生成ApplicationInfo对象
     * @param filePath
     * @return
     */
    private ApplicationInfo parseApplicationInfo(String filePath) {
        try {
            //1、拿到PackageParser对象
            Class packageParserClass = Class.forName("android.content.pm.PackageParser");
            Object packageParser = packageParserClass.newInstance();

            //2、调用PackageParser的 parsePackage() 生成一个Package对象
            Method parsePackageMethod = packageParserClass.getDeclaredMethod("parsePackage", File.class, int.class);
            Object packageObj =  parsePackageMethod.invoke(packageParser, new File(filePath), PackageManager.GET_ACTIVITIES);

            //3、调用PackageParser的 generateApplicationInfo() 生成一个ApplicationInfo对象
            Class packageUserStateClass = Class.forName("android.content.pm.PackageUserState");
            Object packageUserState= packageUserStateClass.newInstance();
            Method generateApplicationInfoMethod = packageParserClass.getDeclaredMethod("generateApplicationInfo",
                    packageObj.getClass(),
                    int.class,
                    packageUserStateClass);

            ApplicationInfo applicationInfo = (ApplicationInfo) generateApplicationInfoMethod.invoke(packageParser,
                    packageObj, 0, packageUserState);

            applicationInfo.sourceDir = filePath;
            applicationInfo.publicSourceDir = filePath;
            return applicationInfo;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }


}
