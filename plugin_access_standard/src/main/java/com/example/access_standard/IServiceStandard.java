package com.example.access_standard;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;


public interface IServiceStandard {

    void attach(Service proxyService);

    void onProxyCreate();
    int onProxyStartCommand(Intent intent, int flags, int startId);
    IBinder onProxyBind(Intent intent);
    boolean onProxyUnbind(Intent intent);
    void onProxyDestroy();

}
