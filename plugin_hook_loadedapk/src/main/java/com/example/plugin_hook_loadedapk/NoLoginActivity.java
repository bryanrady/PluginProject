package com.example.plugin_hook_loadedapk;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class NoLoginActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_no_login);
        Toast.makeText(this, "这是不需要登录的界面", Toast.LENGTH_SHORT).show();

        //我们在插件中可以随便使用上下文，
    }

}
