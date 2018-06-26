package com.mode.checkProduct.commoninfo;

/*
 * 本类用于切换1688API，对应的index，保证api max不发生，保证切换key与secret时，线程同步.
 */
public class Switch1688KeyIndex {

    private Switch1688KeyIndex() {
    };

    // 真正的实现了同步延迟加载
    private static class innerClass {
        static final Switch1688KeyIndex INSTANCE = new Switch1688KeyIndex();
    }

    public static Switch1688KeyIndex getInstance() {
        return innerClass.INSTANCE;
    }

    public synchronized void change1688AppIndex(int index) {
        if (index == ConfigInfo.appIndex) {
            ++ConfigInfo.appIndex;
        }
    }

    public static void main(String[] args) {
        Switch1688KeyIndex aa = Switch1688KeyIndex.getInstance();
        System.out.println(aa.hashCode());
        Switch1688KeyIndex aa1 = Switch1688KeyIndex.getInstance();
        System.out.println(aa1.hashCode());

    }

}
