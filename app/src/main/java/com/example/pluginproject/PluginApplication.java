package com.example.pluginproject;

import android.app.Application;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.os.Environment;
import android.util.Log;

import com.example.pluginproject.hook.ams.HookAmsUtil;
import com.example.pluginproject.hook.loadedapk.HookLoadedApkUtil;
import com.example.pluginproject.hook.loadedapk.LoadedApkPluginManager;
import com.example.pluginproject.hook.merge.HookMergeUtil;
import com.example.pluginproject.hook.merge.MergePluginManager;

import java.io.File;

public class PluginApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        //1.Hook AMS实现集中式登录
//        HookAmsUtil.hookStartActivity(this);
//        HookAmsUtil.hookHandleMessage(this);

        //2.Hook 加载完整的插件apk实现集中式登录
        HookMergeUtil.hookStartActivity(this);
        HookMergeUtil.hookHandleMessage(this);
        String fileName = "plugin_hook_merge.apk";
        String filePath = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/plugin",
                fileName).getAbsolutePath();
        //融合dex和资源
        MergePluginManager.getInstance().mergeDex(this, filePath);
        MergePluginManager.getInstance().mergeResources(this, filePath);

        /**
         * 这种通过融合宿主和插件的dexElements数组加载插件的方式有一定的弊端？
         * 1、如果宿主和插件的类同名(同包同名)，虽然不会报错，但是会导致问题，类加载器只会加载前面一个类，而后面的类就不会的加载，
         *    这样就达不到加载指定的类。Tinker热修复就是通过这样的原理解决。
         *
         * 2、如果宿主要加载很多插件，那这样的话就会把所有插件的dexElements数组都进行合并，这样会导致内存爆棚，而且加载速度会变得
         *    很慢，这样显然达不到加载海量插件的目的。
         *
         * 3、而且如果插件中出现了异常，就会影响到宿主，因为融合后的dexElements数组都是通过系统的PathClassLoader来进行加载的。
         *
         *    如何解决上面这样的问题？
         *
         *      动态加载、按需加载，不是一上来就把所有插件都加载到内存中。
         *
         *      case LAUNCH_ACTIVITY:
         *
         *       // r.packageInfo就是一个LoadedApk  一个apk文件 对应 一个LoadedApk对象
         *       r.packageInfo = getPackageInfoNoCheck(r.activityInfo.applicationInfo, r.compatInfo);
         *       handleLaunchActivity(r, null, "LAUNCH_ACTIVITY");
         *
         *       通过 getPackageInfoNoCheck 这个方法后就会生成一个LoadedApk对象，并且把这个对象以apk的包名为键，
         *       添加进 mPackages 这个map集合中， 这个集合被 ActivityThread 持有。
         *       final ArrayMap<String, WeakReference<LoadedApk>> mPackages = new ArrayMap<String, WeakReference<LoadedApk>>();
         *
         *       private Activity performLaunchActivity(ActivityClientRecord r, Intent customIntent) {
         *          // 一个LoadedApk对应一个ClassLoader
         *          java.lang.ClassLoader cl = r.packageInfo.getClassLoader();
         *          //这个代码实际上就是通过classLoader加载类
         *          activity = mInstrumentation.newActivity(cl, component.getClassName(), r.intent);
         *       }
         *
         *      1.拿到系统的ActivityThread对象，找到ActivityThread对象中的 mPackages 这个map集合。
         *      2.根据插件apk生成一个LoadedApk对象。
         *      3.将插件的LoadedApk对象添加到map集合中。
         */

        //3.Hook 通过LoadedApk加载插件实现解耦
//        HookLoadedApkUtil.hookStartActivity(this);
//        HookLoadedApkUtil.hookHandleMessage(this);
//        fileName = "plugin_hook_loaded_apk.apk";
//        filePath = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/plugin",
//                fileName).getAbsolutePath();
//        LoadedApkPluginManager.getInstance().putLoadedApk2Map(this, filePath);
    }


    @Override
    public AssetManager getAssets() {
        if (MergePluginManager.getInstance().getAssets() != null){
            return MergePluginManager.getInstance().getAssets();
        }
        return super.getAssets();
    }

    @Override
    public Resources getResources() {
        if (MergePluginManager.getInstance().getResources() != null){
            return MergePluginManager.getInstance().getResources();
        }
        return super.getResources();
    }
}
