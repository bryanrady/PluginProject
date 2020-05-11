package com.example.plugin_instrumentation.base;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.example.access_standard.IServiceStandard;

import androidx.annotation.Nullable;

public class BaseService extends Service implements IServiceStandard {

    private Service mThat;

    @Override
    public void attach(Service proxyService) {
        this.mThat = proxyService;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}
