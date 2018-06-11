package com.mode.checkProduct.threadcontrol;

import java.util.concurrent.CountDownLatch;

import com.mode.checkProduct.commoninfo.ConfigInfo;

//使用单例模式，保证在同一个jvm的全局范围内调用的是同一个对象
public class ThreadCountTask {

    public static CountDownLatch endGate = null;
    private static ThreadCountTask threadCountTask = null;

    private ThreadCountTask() {
    };// 默认构造方法设置为私有

    public static ThreadCountTask getInstance() {
        if (threadCountTask == null) {
            threadCountTask = new ThreadCountTask();
            ThreadCountTask.endGate = new CountDownLatch(
                    ConfigInfo.threadNum1688 + ConfigInfo.threadNumOther);
        }
        return threadCountTask;
    }

    public void endGateCountDown() {
        endGate.countDown();
    }

    public void endGateCountAwait() throws InterruptedException {
        endGate.await();
    }

    static int a = 10000;

    public static void main(String[] args) throws Exception {
        Thread thread0 = new Thread() {
            public void run() {
                while (a != 0) {
                    try {
                        Thread.currentThread().join();
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    System.out.println("thread0");
                }
            }
        };
        thread0.start();
        ;
        for (int i = 0; i < 10; i++) {
            Thread thread = new Thread() {
                public void run() {
                    a--;
                }
            };
            thread.start();
        }
    }

}
