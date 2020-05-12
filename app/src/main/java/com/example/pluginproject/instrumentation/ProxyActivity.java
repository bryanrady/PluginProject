package com.example.pluginproject.instrumentation;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;

import com.example.access_standard.IActivityStandard;

import java.lang.reflect.Constructor;

import androidx.annotation.Nullable;

public class ProxyActivity extends Activity {

    private IActivityStandard mIActivityStandard;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //这个类名就是要启动的插件apk的类名
        String activityName = getIntent().getStringExtra("activityName");
        Bundle bundle = getIntent().getBundleExtra("bundle");

        //Class.forName(mActivityName); 这里不能通过反射去获得插件Activity的全类名，因为没有被安装到手机上
        try {
            Class activityClass = getClassLoader().loadClass(activityName);
            Constructor activityClassConstructor = activityClass.getConstructor(new Class[]{});
            Object newInstance = activityClassConstructor.newInstance(new Object[]{});

            //这里直接进行强转
            mIActivityStandard = (IActivityStandard) newInstance;
            mIActivityStandard.attach(this);
            mIActivityStandard.onCreate(bundle);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        mIActivityStandard.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mIActivityStandard.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mIActivityStandard.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mIActivityStandard.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mIActivityStandard.onDestroy();
    }

    @Override
    public ClassLoader getClassLoader() {
        return InstrumentationManager.getInstance().getDexClassLoader();
    }

    @Override
    public Resources getResources() {
        return InstrumentationManager.getInstance().getResources();
    }

    /**
     * 插件中Activity之间的跳转，启动ProxyActivity，最后又会根据类名创建出activity
     * @param intent
     */
    @Override
    public void startActivity(Intent intent) {
        String activityName = intent.getStringExtra("activityName");
        Intent m = new Intent(this, ProxyActivity.class);
        m.putExtra("activityName", activityName);
        super.startActivity(m);
    }
}
