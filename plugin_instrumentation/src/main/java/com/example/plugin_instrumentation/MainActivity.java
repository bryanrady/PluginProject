package com.example.plugin_instrumentation;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.plugin_instrumentation.base.BaseActivity;

public class MainActivity extends BaseActivity {

    @Override
    public void onProxyCreate(Bundle savedInstanceState) {
        super.onProxyCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String name = savedInstanceState.getString("name");
        int age = savedInstanceState.getInt("age");
        Toast.makeText(mThat,"我是"+ name + ",今年+" + age + "了", Toast.LENGTH_SHORT).show();

        findViewById(R.id.btn_jumpSecond).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mThat, SecondActivity.class));
            }
        });
        findViewById(R.id.btn_startService).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startService(new Intent(mThat, FirstService.class));
            }
        });
        findViewById(R.id.btn_sendDynamicReceiver).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }


}
