package com.example.plugin_instrumentation;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.plugin_instrumentation.base.BaseActivity;

public class MainActivity extends BaseActivity implements View.OnClickListener {

    private Button mBtnJumpSecond;
    private Button mBtnStartService;
    private Button mBtnSendDynamicReceiver;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mBtnJumpSecond = findViewById(R.id.btn_jumpSecond);
        mBtnStartService = findViewById(R.id.btn_startService);
        mBtnSendDynamicReceiver = findViewById(R.id.btn_sendDynamicReceiver);

        String name = savedInstanceState.getString("name");
        int age = savedInstanceState.getInt("age");
        Toast.makeText(mThat,"我是"+ name + ",今年+" + age + "了",Toast.LENGTH_SHORT).show();

        mBtnJumpSecond.setOnClickListener(this);
        mBtnStartService.setOnClickListener(this);
        mBtnSendDynamicReceiver.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_jumpSecond:
                startActivity(new Intent(mThat, SecondActivity.class));
                break;
            case R.id.btn_startService:
                break;
            case R.id.btn_sendDynamicReceiver:
                break;
        }
    }
}
