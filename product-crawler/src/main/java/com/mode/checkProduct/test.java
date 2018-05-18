package com.mode.checkProduct;

public class test implements Runnable {

    public static void main(String[] args) throws InterruptedException {
        // TODO Auto-generated method stub

        // test test = new test();
        // Thread thread = new Thread(test);
        //
        // thread.start();
        // thread.sleep(1000);
        // System.out.println(thread.currentThread().getName());
        Thread.currentThread().sleep(1000);
        System.out.println(111);
        Thread.currentThread().sleep(1000);
        System.out.println(111);
        Thread.currentThread().sleep(1000);
        System.out.println(111);

    }

    @Override
    public void run() {
        // TODO Auto-generated method stub
        System.out.println("term");
    }

}
