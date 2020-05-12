package com.example.plugin_instrumentation.base;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.example.access_standard.IServiceStandard;

import androidx.annotation.Nullable;

public class BaseService extends Service implements IServiceStandard {

    protected Service mThat;

    @Override
    public void attach(Service proxyService) {
        this.mThat = proxyService;
    }

    @Override
    public void onProxyCreate() {

    }

    @Override
    public int onProxyStartCommand(Intent intent, int flags, int startId) {
        return Service.START_STICKY;
    }

    @Override
    public IBinder onProxyBind(Intent intent) {
        return null;
    }

    @Override
    public boolean onProxyUnbind(Intent intent) {
        return false;
    }

    @Override
    public void onProxyDestroy() {

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}
