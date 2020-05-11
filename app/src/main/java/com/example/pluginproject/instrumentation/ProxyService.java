package com.example.pluginproject.instrumentation;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.example.access_standard.IServiceStandard;

import androidx.annotation.Nullable;

public class ProxyService extends Service {

    private IServiceStandard mIServiceStandard;



    @Override
    public void onCreate() {
        super.onCreate();
        mIServiceStandard.onCreate();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mIServiceStandard.onBind(intent);
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return mIServiceStandard.onUnbind(intent);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return mIServiceStandard.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mIServiceStandard.onDestroy();
    }
}
