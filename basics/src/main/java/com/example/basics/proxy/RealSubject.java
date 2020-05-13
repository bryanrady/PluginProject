package com.example.basics.proxy;

/**
 * 被代理对象    真实的对象
 */
public class RealSubject implements Subject {

    @Override
    public void eat() {
        System.out.println("RealSubject eat..........");
    }

    @Override
    public void sleep() {
        System.out.println("RealSubject sleep..........");
    }

    @Override
    public void play(String way) {
        System.out.println("RealSubject play.........." + way);
    }
}
