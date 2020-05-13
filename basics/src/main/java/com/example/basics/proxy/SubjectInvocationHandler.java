package com.example.basics.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 *  InvocationHandler相当于一个代理处理器。每一个动态代理类的实例 都必须要实现 InvocationHandler 接口
 *  SubjectInvocationHandler并不是真正的代理类，而是用于定义代理类需要扩展、增强那些方法功能的类。
 *  在invoke函数中，对代理对象的所有方法的调用都被转发至该函数处理。在这里可以灵活的自定义各种你能想到的逻辑。
 */
public class SubjectInvocationHandler implements InvocationHandler {

    //被代理对象
    private Subject mSubject;

    public SubjectInvocationHandler(Subject subject){
        this.mSubject = subject;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        //第3各参数是方法中携带的参数
        if (args != null){
            for (Object arg : args){
                System.out.println("arg == " + arg);
            }
        }

        //在代理真实对象前我们可以添加一些自己的操作
        System.out.println("DynamicProxySubject " + method.getName() + " start");

        //当代理对象调用真实对象的方法时，它会自动的跳转到代理对象关联的InvocationHandler对象的invoke方法来进行调用
        method.invoke(mSubject, args);

        //在代理真实对象后我们也可以添加一些自己的操作
        System.out.println("DynamicProxySubject " + method.getName() + " end");

        return null;
    }
}
