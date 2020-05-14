package com.example.plugin_hook_merge;

import android.app.Activity;
import android.content.res.AssetManager;
import android.content.res.Resources;

public class BaseActivity extends Activity {

    @Override
    public AssetManager getAssets() {
        //而且这个application是宿主的application，不是插件自己的application
        if (getApplication() != null && getApplication().getAssets() != null){
            return getApplication().getAssets();
        }
        return super.getAssets();
    }

    @Override
    public Resources getResources() {
        if (getApplication() != null && getApplication().getResources() != null){
            return getApplication().getResources();
        }
        return super.getResources();
    }
}
