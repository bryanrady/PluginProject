package com.example.pluginproject.instrumentation;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.example.access_standard.IServiceStandard;

import java.lang.reflect.Constructor;

import androidx.annotation.Nullable;

public class ProxyService extends Service {

    private IServiceStandard mIServiceStandard;

    private void init(Intent intent){
        String serviceName = intent.getStringExtra("serviceName");
        try {
            Class loadClass = getClassLoader().loadClass(serviceName);
            Constructor constructor = loadClass.getConstructor(new Class[]{});
            Object instance = constructor.newInstance(new Object[]{});

            mIServiceStandard = (IServiceStandard) instance;
            mIServiceStandard.attach(this);
            mIServiceStandard.onProxyCreate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        if (mIServiceStandard == null){
            init(intent);
        }
        return mIServiceStandard.onProxyBind(intent);
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return mIServiceStandard.onProxyUnbind(intent);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (mIServiceStandard == null){
            init(intent);
        }
        return mIServiceStandard.onProxyStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mIServiceStandard.onProxyDestroy();
    }

    @Override
    public ClassLoader getClassLoader() {
        return InstrumentationManager.getInstance().getDexClassLoader();
    }
    
}
