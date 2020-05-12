package com.example.plugin_instrumentation.base;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.example.access_standard.IActivityStandard;

import androidx.annotation.NonNull;

public class BaseActivity extends Activity implements IActivityStandard {

    protected Activity mThat;

    @Override
    public void attach(Activity proxyActivity) {
        this.mThat = proxyActivity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

    }

    @Override
    public void onStart() {

    }

    @Override
    public void onResume() {

    }

    @Override
    public void onPause() {

    }

    @Override
    public void onStop() {

    }

    @Override
    public void onDestroy() {

    }

    /**
     * 凡是只要在插件apk中用到activity的方法有涉及到上下文的方法都需要重写
     */

    @Override
    public void setContentView(View view) {
        if (mThat != null){
            mThat.setContentView(view);
        }else{
            super.setContentView(view);
        }
    }

    @Override
    public void setContentView(int layoutResID) {
        if (mThat != null){
            mThat.setContentView(layoutResID);
        }else{
            super.setContentView(layoutResID);
        }
    }

    @Override
    public <T extends View> T findViewById(int id) {
        if (mThat != null){
            return mThat.findViewById(id);
        }else{
            return super.findViewById(id);
        }
    }

    @NonNull
    @Override
    public LayoutInflater getLayoutInflater() {
        if (mThat != null){
            return mThat.getLayoutInflater();
        }else{
            return super.getLayoutInflater();
        }
    }

    @Override
    public ClassLoader getClassLoader() {
        if (mThat != null){
            return mThat.getClassLoader();
        }else{
            return super.getClassLoader();
        }
    }

    @Override
    public String getPackageName() {
        if (mThat != null){
            return mThat.getPackageName();
        }else{
            return super.getPackageName();
        }
    }

    @Override
    public PackageManager getPackageManager() {
        if (mThat != null){
            return mThat.getPackageManager();
        }else{
            return super.getPackageManager();
        }
    }

    @Override
    public Resources getResources() {
        if (mThat != null){
            return mThat.getResources();
        }else{
            return super.getResources();
        }
    }

    @Override
    public AssetManager getAssets() {
        if (mThat != null){
            return mThat.getAssets();
        }else{
            return super.getAssets();
        }
    }

    @Override
    public ApplicationInfo getApplicationInfo() {
        if (mThat != null){
            return mThat.getApplicationInfo();
        }else{
            return super.getApplicationInfo();
        }
    }

    @Override
    public Intent getIntent() {
        if (mThat != null){
            return mThat.getIntent();
        }else{
            return super.getIntent();
        }
    }

    @Override
    public void startActivity(Intent intent) {
        if (mThat != null){
            //这里就会调用到ProxyActivity的startActivity()，activityName 要跳转的类名
            Intent m = new Intent();
            m.putExtra("activityName", intent.getComponent().getClassName());
            mThat.startActivity(m);
        }else{
            super.startActivity(intent);
        }
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        if (mThat != null){
            mThat.startActivityForResult(intent, requestCode);
        }else{
            super.startActivityForResult(intent, requestCode);
        }
    }

    @Override
    public ComponentName startService(Intent service) {
        if (mThat != null){
            return mThat.startService(service);
        }else{
            return super.startService(service);
        }
    }

    @Override
    public boolean bindService(Intent service, ServiceConnection conn, int flags) {
        if (mThat != null){
            return mThat.bindService(service, conn, flags);
        }else{
            return super.bindService(service, conn, flags);
        }
    }

    @Override
    public void sendBroadcast(Intent intent) {
        if (mThat != null){
            mThat.sendBroadcast(intent);
        }else{
            super.sendBroadcast(intent);
        }
    }

    @Override
    public Intent registerReceiver(BroadcastReceiver receiver, IntentFilter filter) {
        if (mThat != null){
            return mThat.registerReceiver(receiver, filter);
        }else{
            return super.registerReceiver(receiver, filter);
        }
    }

    @Override
    public void unregisterReceiver(BroadcastReceiver receiver) {
        if (mThat != null){
            mThat.unregisterReceiver(receiver);
        }else{
            super.unregisterReceiver(receiver);
        }
    }

}
