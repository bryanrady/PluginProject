package com.example.plugin_instrumentation;

import android.widget.Toast;

import com.example.plugin_instrumentation.base.BaseService;

public class FirstService extends BaseService {

    @Override
    public void onProxyCreate() {
        super.onProxyCreate();
        Toast.makeText(mThat, "服务已经启动成功", Toast.LENGTH_SHORT).show();
    }

}
