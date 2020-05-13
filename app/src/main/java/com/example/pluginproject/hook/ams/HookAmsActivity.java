package com.example.pluginproject.hook.ams;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.Toast;

import com.example.pluginproject.BaseActivity;
import com.example.pluginproject.R;

public class HookAmsActivity extends BaseActivity {

    @Override
    public int bindLayout() {
        return R.layout.activity_hook_ams;
    }

    @Override
    public void initView() {

    }

    @Override
    public void doBusiness(Context context) {

    }

    /**
     * 这几个要跳转的Activity和登录Activity是没有在AndroidManifest.xml文件中进行注册的
     * @param view
     */
    public void jump1(View view) {
        startActivity(new Intent(this, AmsFirstActivity.class));
    }

    public void jump2(View view) {
        startActivity(new Intent(this, AmsSecondActivity.class));
    }

    public void jump3(View view) {
        startActivity(new Intent(this, AmsThirdActivity.class));
    }

    public void logout(View view) {
        SharedPreferences sharedPreferences = this.getSharedPreferences("bryanrady", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("login",false);
        editor.apply();
        Toast.makeText(this, "退出登录成功",Toast.LENGTH_SHORT).show();
    }
}
