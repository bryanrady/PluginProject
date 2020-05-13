package com.example.pluginproject;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Button;

import com.example.pluginproject.hook.HookActivity;
import com.example.pluginproject.instrumentation.InstrumentationActivity;
import com.example.pluginproject.skin.SkinActivity;

import butterknife.BindView;
import butterknife.OnClick;

public class MainActivity extends BaseActivity {

    @BindView(R.id.btn_skin)
    Button mBtnSkin;

    @BindView(R.id.btn_instrumentation)
    Button mBtnInstrumentation;

    @BindView(R.id.btn_hook)
    Button mBtnHook;

    @Override
    public int bindLayout() {
        return R.layout.activity_main;
    }

    @Override
    public void initView() {

    }

    @Override
    public void doBusiness(Context context) {

    }

    @OnClick({R.id.btn_skin, R.id.btn_instrumentation, R.id.btn_hook})
    void doClick(View view){
        Intent intent;
        switch (view.getId()){
            case R.id.btn_skin:
                startActivity(new Intent(this, SkinActivity.class));
                break;
            case R.id.btn_instrumentation:
                startActivity(new Intent(this, InstrumentationActivity.class));
                break;
            case R.id.btn_hook:
                startActivity(new Intent(this, HookActivity.class));
                break;
        }
    }

}
