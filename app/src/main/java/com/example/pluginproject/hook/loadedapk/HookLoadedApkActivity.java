package com.example.pluginproject.hook.loadedapk;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.Toast;

import com.example.pluginproject.BaseActivity;
import com.example.pluginproject.R;

public class HookLoadedApkActivity extends BaseActivity {

    @Override
    public int bindLayout() {
        return R.layout.activity_hook_loadedapk;
    }

    @Override
    public void initView() {

    }

    @Override
    public void doBusiness(Context context) {

    }

    public void jump1(View view) {
        Intent intent = new Intent();
        intent.setComponent(new ComponentName("com.example.plugin_loadedapk",
                "com.example.plugin_loadedapk.LoadedApkFirstActivity"));
        startActivity(intent);
    }

    public void jump2(View view) {
        Intent intent = new Intent();
        intent.setComponent(new ComponentName("com.example.plugin_loadedapk",
                "com.example.plugin_loadedapk.LoadedApkSecondActivity"));
        startActivity(intent);
    }

    public void jump3(View view) {
        Intent intent = new Intent();
        intent.setComponent(new ComponentName("com.example.plugin_loadedapk",
                "com.example.plugin_loadedapk.LoadedApkThirdActivity"));
        startActivity(intent);
    }

    public void jump_no_login(View view) {
        Intent intent = new Intent();
        intent.setComponent(new ComponentName("com.example.plugin_loadedapk",
                "com.example.plugin_loadedapk.LoadedApkNoLoginActivity"));
        startActivity(intent);
    }

    public void logout(View view) {
        SharedPreferences sharedPreferences = this.getSharedPreferences("bryanrady", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("login",false);
        editor.apply();
        Toast.makeText(this, "退出登录成功",Toast.LENGTH_SHORT).show();
    }
}