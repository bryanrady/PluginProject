package com.example.access_standard;

import android.app.Activity;
import android.os.Bundle;

/**
 * 插件Activity的生命周期依赖于宿主的代理Activity生命周期
 */
public interface IActivityStandard {

    void attach(Activity proxyActivity);

    //生命周期的管理
    void onProxyCreate(Bundle savedInstanceState);
    void onProxyStart();
    void onProxyResume();
    void onProxyPause();
    void onProxyStop();
    void onProxyDestroy();

}
