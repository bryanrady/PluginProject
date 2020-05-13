package com.example.pluginproject;

import android.app.Application;

import com.example.pluginproject.hook.ams.HookAmsUtil;

public class PluginApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        HookAmsUtil.hookStartActivity(this);
        HookAmsUtil.hookHandleMessage(this);
    }

}
