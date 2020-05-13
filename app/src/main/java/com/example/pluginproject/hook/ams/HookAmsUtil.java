package com.example.pluginproject.hook.ams;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import androidx.annotation.NonNull;


public class HookAmsUtil {

    /**
     * 首先Hook系统的startActivity，将未注册的组件的意图
     *  如果我们要跳转到未注册的Activity中，首先要Hook系统的startActivity，在Activity没有加载之前将未注册的Activity意图放进
     *  一个已经注册了的ProxyActivity中，就是将跳转的真实信息隐藏到ProxyActivity的Intent中，然后将系统原来的intent替换为
     *  ProxyActivity的intent,这样就能绕开AMS检查。
     *
     *  然后在要加载Activity的时候将原来的隐藏在ProxyActivity的Intent的真是意图进行还原，然后再执行系统的加载Activity的方法，
     *  这样就能达到跳转未注册的Activity界面。
     *
     * @param context
     */
    public static void hookStartActivity(Context context){
        try {
            //1、找到ActivityManagerNative类中的gDefault对象
            Class activityManagerNativeClass = Class.forName("android.app.ActivityManagerNative");
            Field gDefaultField = activityManagerNativeClass.getDeclaredField("gDefault");
            gDefaultField.setAccessible(true);
            Object gDefault = gDefaultField.get(null);

            //2、找到mInstance对象，这个就是一个IActivityManager对象
            Class singleTonClass = Class.forName("android.util.Singleton");
            Field mInstanceField = singleTonClass.getDeclaredField("mInstance");
            mInstanceField.setAccessible(true);
            Object mInstance = mInstanceField.get(gDefault);

            //3、找到系统中的IActivityManager对象
            Object realIActivityManager =  mInstance;

            //4、动态代理创建IActivityManager代理对象，代理对象需要实现IActivityManager对象的所有接口
            Class iActivityManagerClass = Class.forName("android.app.IActivityManager");
            Object proxyIActivityManager = Proxy.newProxyInstance(
                    context.getClassLoader(),
                    new Class[]{iActivityManagerClass},
                    new IActivityManagerInvocationHandler(context, realIActivityManager));

            //5.将系统的IActivityManager换成我们自己的IActivityManager
            mInstanceField.set(gDefault, proxyIActivityManager);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static class IActivityManagerInvocationHandler implements InvocationHandler{

        private Context mContext;
        private Object mRealIActivityManager;

        public IActivityManagerInvocationHandler(Context context, Object realIActivityManager){
            this.mContext = context;
            this.mRealIActivityManager = realIActivityManager;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            if ("startActivity".equals(method.getName())){
                if (args != null){
                    //先找到Intent
                    int index = -1;
                    Intent originalIntent = null;
                    for (int i = 0; i < args.length; i++){
                        if (args[i] instanceof Intent){
                            originalIntent = (Intent) args[i];
                            index = i;
                            break;
                        }
                    }
                    //说明找到了原来的Intent和要跳转的组件
                    if (originalIntent != null && originalIntent.getComponent() != null){
                        //我们创建一个新的Intent，将新的Intent替换老的Intent
                        Intent newIntent = new Intent(mContext, AmsProxyActivity.class);
                        newIntent.putExtra("originalIntent", originalIntent);
                        args[index] = newIntent;
                    }

                }
            }
            //对每个方法进行转发
            return method.invoke(mRealIActivityManager, args);
        }
    }

    public static void hookHandleMessage(Context context){
        try {
            //1、找到ActivityThread类中的sCurrentActivityThread对象
            Class activityThreadClass = Class.forName("android.app.ActivityThread");
            Field sCurrentActivityThreadField = activityThreadClass.getDeclaredField("sCurrentActivityThread");
            sCurrentActivityThreadField.setAccessible(true);
            Object sCurrentActivityThread = sCurrentActivityThreadField.get(null);
            
            //2、找到ActivityThread类中的mH对象
            Field mHField = activityThreadClass.getDeclaredField("mH");
            mHField.setAccessible(true);
            Handler mH = (Handler) mHField.get(sCurrentActivityThread);

            //3、找到Handler类中的mCallback接口
            Field mCallbackField = Handler.class.getDeclaredField("mCallback");
            mCallbackField.setAccessible(true);

            //4、通过接口实现得到自己的Callback
            InterfaceMHCallback interfaceMHCallback = new InterfaceMHCallback(context, mH);

            //5、替换系统中Handler的Callback接口
            mCallbackField.set(mH, interfaceMHCallback);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static class InterfaceMHCallback implements Handler.Callback{

        private Context mContext;
        private Handler mRealHandler;

        public InterfaceMHCallback(Context context, Handler realHandler){
            this.mContext = context;
            this.mRealHandler = realHandler;
        }

        @Override
        public boolean handleMessage(@NonNull Message msg) {
            //100代表LAUNCH_ACTIVITY
            if (msg.what == 100){
                //在这里面处理自己的逻辑
                handleLaunchActivity(mContext, msg);
            }

            //处理完成后，继续执行系统的流程
            mRealHandler.handleMessage(msg);
            //这里要返回true，表示我们通过自己的接口来处理handle的消息
            return true;
        }
    }

    private static void handleLaunchActivity(Context context, Message msg) {
        try {
            Object activityClientRecord = msg.obj;
            Field intentField = activityClientRecord.getClass().getDeclaredField("intent");
            intentField.setAccessible(true);
            Object intent = intentField.get(activityClientRecord);

            //这个Intent是前面包装了的newIntent
            Intent newIntent = (Intent) intent;

            if (newIntent != null) {
                Intent originalIntent = newIntent.getParcelableExtra("originalIntent");
                if (originalIntent != null) {
                    SharedPreferences sharedPreferences = context.getSharedPreferences("bryanrady", Context.MODE_PRIVATE);
                    boolean isLogin = sharedPreferences.getBoolean("login", false);
                    if (isNeedLoginActivity(originalIntent)){
                        if (isLogin) {
                            //如果已经登录过了
                            newIntent.setComponent(originalIntent.getComponent());
                        } else {
                            //如果之前没有登录，就改变意图前往登录
                            ComponentName componentName = new ComponentName(context, AmsLoginActivity.class);
                            //顺便把之前要跳转的组件传给AmsLoginActivity，登录成功后跳转原来的的组件
                            newIntent.putExtra("classname", originalIntent.getComponent().getClassName());
                            newIntent.setComponent(componentName);
                        }
                    }else{
                        //不需要登录的Activity直接使用原来的意图
                        newIntent.setComponent(originalIntent.getComponent());
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 是否需要登录
     * @param intent
     * @return
     */
    private static boolean isNeedLoginActivity(Intent intent){
        final String[] classNames = {
                AmsFirstActivity.class.getName(),
                AmsSecondActivity.class.getName(),
                AmsThirdActivity.class.getName(),
        };
        for (String className : classNames){
            if (className.equals(intent.getComponent().getClassName())){
                return true;
            }
        }
        return false;
    }


}
