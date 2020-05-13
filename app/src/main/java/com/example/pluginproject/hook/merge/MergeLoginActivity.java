package com.example.pluginproject.hook.merge;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.pluginproject.R;

public class MergeLoginActivity extends Activity {

    private EditText mEtUsername;
    private EditText mEtPassword;

    private SharedPreferences mSharedPreferences;
    private String mClassName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ams_login);

        mEtUsername = findViewById(R.id.et_username);
        mEtPassword = findViewById(R.id.et_password);

        mSharedPreferences = this.getSharedPreferences("bryanrady", Context.MODE_PRIVATE);
        mClassName = getIntent().getStringExtra("classname");
    }

    public void login(View view) {
        String username = mEtUsername.getText().toString().trim();
        String password = mEtPassword.getText().toString().trim();
        if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
            Toast.makeText(this, "请填写用户名或密码",Toast.LENGTH_SHORT).show();
            return;
        }
        if ("bryanrady".equals(username) && "123456".equals(password)) {
            SharedPreferences.Editor editor = mSharedPreferences.edit();
            editor.putString("username", username);
            editor.putString("password", password);
            editor.putBoolean("login",true);
            editor.apply();

            Toast.makeText(this, "登录成功",Toast.LENGTH_SHORT).show();

            if (mClassName != null) {
                ComponentName componentName = new ComponentName(this, mClassName);
                Intent intent = new Intent();
                intent.setComponent(componentName);
                startActivity(intent);
                finish();
            }
        }else{
            SharedPreferences.Editor editor = mSharedPreferences.edit();
            editor.putBoolean("login",false);
            editor.apply();
            Toast.makeText(this, "登录失败",Toast.LENGTH_SHORT).show();
        }
    }

}
