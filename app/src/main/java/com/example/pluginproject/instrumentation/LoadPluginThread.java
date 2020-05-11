package com.example.pluginproject.instrumentation;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageItemInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.os.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;

import dalvik.system.DexClassLoader;

public class LoadPluginThread extends Thread {

    private Context mContext;
    private boolean mLoadSuccess = false;
    private PackageInfo mPackageInfo;
    private Resources mResources;
    private DexClassLoader mDexClassLoader;

    public LoadPluginThread(Context context){
        this.mContext = context;
    }

    @Override
    public void run() {
        super.run();
        copyFileToPrivateDir(mContext);
        loadPlugin(mContext);
    }

    /**
     * 将插件apk从外置卡copy到程序私有目录下面
     * @param context
     */
    private void copyFileToPrivateDir(Context context){
        String fileName = "plugin_instrumentation.apk";
        File fileDir = context.getDir("plugin", Context.MODE_PRIVATE);
        String filePath = new File(fileDir, fileName).getAbsolutePath();
        File file = new File(filePath);
        if(file.exists()){
            file.delete();
        }
        InputStream is = null;
        FileOutputStream fos = null;
        try {
            is = new FileInputStream(new File(Environment.getExternalStorageDirectory() + "/plugin",
                    fileName).getAbsolutePath());
            fos = new FileOutputStream(filePath);
            int len;
            byte[] buffer = new byte[1024];
            while ((len = is.read(buffer)) != -1){
                fos.write(buffer,0, len);
            }

            //加载插件
            loadPlugin(context);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (is != null){
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (fos != null){
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void loadPlugin(Context context){
        File fileDir = context.getDir("plugin", Context.MODE_PRIVATE);
        String fileName = "plugin_instrumentation.apk";
        String dexPath = new File(fileDir, fileName).getAbsolutePath();

        PackageManager packageManager = context.getPackageManager();
        mPackageInfo = packageManager.getPackageArchiveInfo(dexPath,PackageManager.GET_ACTIVITIES);

        File dexOutFile = context.getDir("dex", Context.MODE_PRIVATE);
        mDexClassLoader = new DexClassLoader(dexPath, dexOutFile.getAbsolutePath(),null, context.getClassLoader());
        try {
            AssetManager assetManager = AssetManager.class.newInstance();
            Method addAssetPath = AssetManager.class.getMethod("addAssetPath", String.class);
            addAssetPath.invoke(assetManager,dexPath);
            mResources = new Resources(assetManager,context.getResources().getDisplayMetrics(),context.getResources().getConfiguration());
            mLoadSuccess = true;
        }catch (Exception e) {
            e.printStackTrace();
            mLoadSuccess = false;
        }

    //    parseReceiver(context,new File(dexPath));
    }

    /**
     * 注册插件中的广播
     * @param context
     * @param packageFile
     * @return
     */
    private void parseReceiver(Context context, File packageFile) {
        try {
            //解析apk包的入口在    PMS.main --> PMS构造函数 -> mAppInstallDir（/data/data目录下） --》scanDirLI();
            // --> scanPackageLI() --> PackageParser.parsePackage() -->PackageParser.Package -->ArrayList<Activity> receivers
            // -->Activity是一个组件Component-->

            //第一步：先拿到PackageParser对象
            Class packageParserClass = Class.forName("android.content.pm.PackageParser");
            Object packageParser = packageParserClass.newInstance();
            //第二步: 拿到PackageParser中的parsePackage方法 执行 parsePackage 方法 得到 Package对象
            Method parsePackageMethod = packageParserClass.getDeclaredMethod("parsePackage",File.class,int.class);
            Object packageObj = parsePackageMethod.invoke(packageParser, packageFile, PackageManager.GET_ACTIVITIES);
            // Package 里面有 receivers 这个字段 public final ArrayList<Activity> receivers = new ArrayList<Activity>(0);
            // 拿到receivers  广播集合   插件app存在多个广播   集合  ArrayList<Activity>  name  ————》 ActivityInfo   className
            Field receiversField = packageObj.getClass().getDeclaredField("receivers");
            ArrayList receivers = (ArrayList) receiversField.get(packageObj);
            Class activityClass = Class.forName("android.content.pm.PackageParser$Activity");

            //第三步: 将得到Activity组件转换成ActivityInfo
            Class packageUserStateClass = Class.forName("android.content.pm.PackageUserState");
            //generateActivityInfo(Activity a, int flags,PackageUserState state, int userId)
            Method generateActivityInfoMethod = packageParserClass.getDeclaredMethod("generateActivityInfo",
                    activityClass, int.class, packageUserStateClass, int.class);
            Object packageUserState = packageUserStateClass.newInstance();

            Class userHandlerClass = Class.forName("android.os.UserHandle");
            Method getCallingUserIdMethod = userHandlerClass.getDeclaredMethod("getCallingUserId");
            int userId = (int) getCallingUserIdMethod.invoke(null);

            //第四步: 将得到Component组件中的intents字段
            Class componentClass = Class.forName("android.content.pm.PackageParser$Component");
            Field intentsField = componentClass.getDeclaredField("intents");

            //第五步: 将receivers集合进行遍历 得到每个receiver的name 和 intentFilter 然后注册广播
            for(Object activity : receivers){
                ActivityInfo activityInfo = (ActivityInfo) generateActivityInfoMethod
                        .invoke(packageParser, activity, 0, packageUserState, userId);

                //ActivityInfo extends ComponentInfo extends PackageItemInfo
                PackageItemInfo packageItemInfo = activityInfo;

                //packageItemInfo.name  -->  Public name of this item. From the "android:name" attribute. -->receiver的全类名
                Class receiverClassName = getDexClassLoader().loadClass(packageItemInfo.name);
                BroadcastReceiver receiver = (BroadcastReceiver) receiverClassName.newInstance();

                //得到每一个组件的intentFilter 一个Receiver可能对应多个intentFilter
                ArrayList<? extends IntentFilter> intents = (ArrayList<? extends IntentFilter>) intentsField.get(activity);

                for (IntentFilter intentFilter : intents) {
                    context.registerReceiver(receiver, intentFilter);
                }
            }
            mLoadSuccess = true;
        } catch (Exception e) {
            e.printStackTrace();
            mLoadSuccess = false;
        }
    }

    public boolean loadSuccess(){
        return mLoadSuccess;
    }

    public DexClassLoader getDexClassLoader() {
        return mDexClassLoader;
    }

    /**
     * 插件apk的资源
     * @return
     */
    public Resources getResources() {
        return mResources;
    }

    /**
     * 插件apk的包信息
     * @return
     */
    public PackageInfo getPackageInfo() {
        return mPackageInfo;
    }

}
