package com.example.plugin_instrumentation;

import android.os.Bundle;

import com.example.plugin_instrumentation.base.BaseActivity;

public class SecondActivity extends BaseActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
    }

}
