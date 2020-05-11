package com.example.plugin_instrumentation;

import android.os.Bundle;
import android.widget.Toast;

import com.example.plugin_instrumentation.base.BaseActivity;

public class MainActivity extends BaseActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String name = savedInstanceState.getString("name");
        int age = savedInstanceState.getInt("age");
        Toast.makeText(mThat,"我是"+ name + ",今年+" + age + "了",Toast.LENGTH_SHORT).show();

    }
}
