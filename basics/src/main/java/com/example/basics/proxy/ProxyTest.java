package com.example.basics.proxy;

import java.lang.reflect.Proxy;

public class ProxyTest {

    public static void main(String[] args) {
        //被代理的对象    真实的对象
        RealSubject realSubject = new RealSubject();

        //静态代理对象
        StaticProxySubject staticProxySubject = new StaticProxySubject(realSubject);
        //调用静态代理对象的方法
        staticProxySubject.eat();
        staticProxySubject.sleep();
        staticProxySubject.play("wangqingbin");

        System.out.println("****************************************");

        //动态代理对象
        Subject dynamicProxySubject = (Subject) Proxy.newProxyInstance(
                ClassLoader.getSystemClassLoader(),
                new Class[]{Subject.class},
                new SubjectInvocationHandler(realSubject)
        );
        dynamicProxySubject.eat();
        dynamicProxySubject.sleep();
        dynamicProxySubject.play("wangqingbin");
    }

}
