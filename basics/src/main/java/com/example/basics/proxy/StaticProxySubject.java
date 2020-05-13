package com.example.basics.proxy;

import com.example.basics.proxy.Subject;

/**
 *
 *  静态代理看起来是比较简单的，它的原理跟装饰模式类似，Subject接口相当于一个抽象构建Component ，被代理类RealSubject相当于
 *  一个具体构建ConcreteComponent，而代理类ProxySubject则相当于装饰角色（Decorator）和具体装饰角色（ConcreteDecorator）的结合。
 *  不管是静态代理和装饰设计模式其实都是为了对原有功能的增强 ，屏蔽或改变。
 *
 * 静态代理（传统代理模）的实现方式比较暴力直接，代理对象需要将被代理对象的所有方法都重写一遍，并且一个个的手动转发过去。
 * 在维护被代理类的同时，还需要维护代理类的相关代码，过程比较累人。
 *
 * 因此就需要我们的动态代理登场了，通过使用动态代理，动态代理能够自动将代理类的相关方法转发到被代理类。
 *
 */
public class StaticProxySubject implements Subject {

    //被代理的对象
    private Subject mSubject;

    public StaticProxySubject(Subject subject){
        this.mSubject = subject;
    }

    @Override
    public void eat() {
        //代理类中还可以加上自己想做的事情
        System.out.println("StaticProxySubject eat start");

        //在代理类的方法中间接访问被代理类的方法
        mSubject.eat();

        System.out.println("StaticProxySubject eat end");
    }

    @Override
    public void sleep() {
        //代理类中还可以加上自己想做的事情
        System.out.println("StaticProxySubject sleep start");

        //在代理类的方法中间接访问被代理类的方法
        mSubject.sleep();

        System.out.println("StaticProxySubject sleep end");
    }

    @Override
    public void play(String way) {
        //代理类中还可以加上自己想做的事情
        System.out.println("StaticProxySubject play start " + way);

        //在代理类的方法中间接访问被代理类的方法
        mSubject.sleep();

        System.out.println("StaticProxySubject play end " + way);
    }
}
