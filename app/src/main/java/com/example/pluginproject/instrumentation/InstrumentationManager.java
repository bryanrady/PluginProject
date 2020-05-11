package com.example.pluginproject.instrumentation;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.res.Resources;

import dalvik.system.DexClassLoader;

public class InstrumentationManager {

    private LoadPluginThread mLoadPluginThread;

    private static final InstrumentationManager INSTANCE = new InstrumentationManager();

    private InstrumentationManager(){

    }

    public static InstrumentationManager getInstance(){
        return INSTANCE;
    }

    public boolean loadPluginPath(Context context){
        mLoadPluginThread = new LoadPluginThread(context);
        mLoadPluginThread.start();
        try {
            mLoadPluginThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return mLoadPluginThread.loadSuccess();
    }

    public DexClassLoader getDexClassLoader(){
        if (mLoadPluginThread != null){
            return mLoadPluginThread.getDexClassLoader();
        }
        return null;
    }

    public PackageInfo getPackageInfo(){
        if (mLoadPluginThread != null){
            return mLoadPluginThread.getPackageInfo();
        }
        return null;
    }

    public Resources getResources(){
        if (mLoadPluginThread != null){
            return mLoadPluginThread.getResources();
        }
        return null;
    }

}
