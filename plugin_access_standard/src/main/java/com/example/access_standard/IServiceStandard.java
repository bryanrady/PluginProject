package com.example.access_standard;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;


public interface IServiceStandard {

    void attach(Service proxyService);

    void onCreate();
    int onStartCommand(Intent intent, int flags, int startId);
    IBinder onBind(Intent intent);
    boolean onUnbind(Intent intent);
    void onDestroy();

}
