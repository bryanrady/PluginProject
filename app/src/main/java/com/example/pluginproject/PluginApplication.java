package com.example.pluginproject;

import android.app.Application;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.os.Environment;

import com.example.pluginproject.hook.ams.HookAmsUtil;
import com.example.pluginproject.hook.loadedapk.HookLoadedApkUtil;

import java.io.File;

public class PluginApplication extends Application {

    private AssetManager mAssetManager;
    private Resources mResources;

    @Override
    public void onCreate() {
        super.onCreate();
        //1.Hook AMS实现集中式登录
        HookAmsUtil.hookStartActivity(this);
        HookAmsUtil.hookHandleMessage(this);

        //2.Hook 加载完整的插件apk实现集中式登录
        HookLoadedApkUtil.hookStartActivity(this);
        HookLoadedApkUtil.hookHandleMessage(this);
        String fileName = "plugin_loadedapk.apk";
        String filePath = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/plugin",
                fileName).getAbsolutePath();
        //融合dex和资源
        mergeDex(filePath);
        mergeResources(filePath);
    }

    /**
     * 合并宿主apk和插件apk的dexElements数组
     * @param filePath
     */
    private void mergeDex(String filePath){

    }

    /**
     * 合并宿主apk和插件apk的资源
     * @param filePath
     */
    private void mergeResources(String filePath) {

    }

    @Override
    public AssetManager getAssets() {
        return mAssetManager != null ? super.getAssets() : super.getAssets();
    }

    @Override
    public Resources getResources() {
        return mResources != null ? mResources : super.getResources();
    }
}
