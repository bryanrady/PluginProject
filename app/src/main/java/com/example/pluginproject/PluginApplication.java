package com.example.pluginproject;

import android.app.Application;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.os.Environment;

import com.example.pluginproject.hook.ams.HookAmsUtil;
import com.example.pluginproject.hook.merge.HookMergeUtil;
import com.example.pluginproject.hook.merge.MergePluginManager;

import java.io.File;

public class PluginApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        //1.Hook AMS实现集中式登录
        HookAmsUtil.hookStartActivity(this);
        HookAmsUtil.hookHandleMessage(this);

        //2.Hook 加载完整的插件apk实现集中式登录
        HookMergeUtil.hookStartActivity(this);
        HookMergeUtil.hookHandleMessage(this);
        String fileName = "plugin_loadedapk.apk";
        String filePath = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/plugin",
                fileName).getAbsolutePath();
        //融合dex和资源
        MergePluginManager.getInstance().mergeDex(this, filePath);
        MergePluginManager.getInstance().mergeResources(this, filePath);


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
