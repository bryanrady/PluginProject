package com.example.pluginproject.instrumentation;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.pluginproject.BaseActivity;
import com.example.pluginproject.R;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;

public class InstrumentationActivity extends BaseActivity {

    @Override
    public int bindLayout() {
        return R.layout.activity_instrumentation;
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

    public void load(View view){
        boolean isLoadSuccess = InstrumentationManager.getInstance().loadPluginPath(this);
        if(isLoadSuccess){
            Intent intent = new Intent(this, ProxyActivity.class);
            intent.putExtra("activityName", InstrumentationManager.getInstance().getPackageInfo().activities[0].name);
            Bundle bundle = new Bundle();
            bundle.putString("name","张三");
            bundle.putInt("age",25);
            intent.putExtra("bundle", bundle);
            startActivity(intent);
        }else{
            Toast.makeText(this,"加载插件出了问题",Toast.LENGTH_SHORT).show();
        }
    }

    public void sendBroadCast(View view){
    }

}
