package com.example.pluginproject.hook.ams;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;

import com.example.pluginproject.R;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import androidx.annotation.NonNull;

/**
 * 9.0以上这些是玩不转的
 */
public class HookAmsUtil {

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
            Field mHField = activityThreadClass.getField("mH");
            mHField.setAccessible(true);
            Object mH = mHField.get(sCurrentActivityThread);

            //3、找到Handler类中的mCallback接口
            Field mCallbackField = Handler.class.getDeclaredField("mCallback");
            mCallbackField.setAccessible(true);

            //4、通过接口实现得到自己的Callback接口


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

                Object activityClientRecord = msg.obj;
            }
            //继续执行系统的流程
            mRealHandler.handleMessage(msg);
            //这里要返回true，表示我们通过自己的接口来处理handle的消息
            return true;
        }
    }

}
