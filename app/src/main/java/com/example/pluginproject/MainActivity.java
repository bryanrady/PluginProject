package com.example.pluginproject;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.view.View;
import android.widget.Button;

import com.example.pluginproject.hook.HookActivity;
import com.example.pluginproject.instrumentation.InstrumentationActivity;
import com.example.pluginproject.skin.SkinActivity;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import butterknife.BindView;
import butterknife.OnClick;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;

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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE) != PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{
                                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                Manifest.permission.READ_EXTERNAL_STORAGE,
                        },
                        0);
            }
        }
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
