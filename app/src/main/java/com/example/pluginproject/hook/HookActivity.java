package com.example.pluginproject.hook;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Button;

import com.example.pluginproject.BaseActivity;
import com.example.pluginproject.R;
import com.example.pluginproject.hook.ams.HookAmsActivity;
import com.example.pluginproject.hook.merge.HookMergeActivity;

import butterknife.BindView;
import butterknife.OnClick;

public class HookActivity extends BaseActivity {

    @BindView(R.id.btn_hook_ams)
    Button btn_hook_ams;

    @BindView(R.id.btn_hook_dex_elements)
    Button btn_hook_dex_elements;

    @BindView(R.id.btn_hook_loadedApk)
    Button btn_hook_loadedApk;

    @OnClick({R.id.btn_hook_ams, R.id.btn_hook_dex_elements, R.id.btn_hook_loadedApk})
    void onClick(View view){
        switch (view.getId()){
            case R.id.btn_hook_ams:
                startActivity(new Intent(this, HookAmsActivity.class));
                break;
            case R.id.btn_hook_dex_elements:
                startActivity(new Intent(this, HookMergeActivity.class));
                break;
            case R.id.btn_hook_loadedApk:
                break;
        }
    }

    @Override
    public int bindLayout() {
        return R.layout.activity_hook;
    }

    @Override
    public void initView() {

    }

    @Override
    public void doBusiness(Context context) {

    }
}
