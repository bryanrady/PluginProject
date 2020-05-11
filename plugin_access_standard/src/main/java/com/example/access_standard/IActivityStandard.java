package com.example.access_standard;

import android.app.Activity;
import android.os.Bundle;

/**
 * 插件Activity的生命周期依赖于宿主的代理Activity生命周期
 */
public interface IActivityStandard {

    void attach(Activity proxyActivity);

    void onCreate(Bundle savedInstanceState);
    void onStart();
    void onResume();
    void onPause();
    void onStop();
    void onDestroy();

}
