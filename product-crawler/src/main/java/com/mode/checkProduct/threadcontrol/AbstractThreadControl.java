package com.mode.checkProduct.threadcontrol;

import java.util.List;

public abstract class AbstractThreadControl implements Runnable {
    protected List<String> dataList;
    protected int start;
    protected int end;
    protected String threadName;
    protected int threadNum;

    public AbstractThreadControl(String threadName, List<String> dataList, int start, int end) {
        this.threadName = threadName;
        this.dataList = dataList;
        this.start = start;
        this.end = end;
    }

    public AbstractThreadControl(List<String> dataList, int threadNum) {
        this.dataList = dataList;
        if (dataList.size() <= threadNum * 2) {
            this.threadNum = 1;
        } else {
            this.threadNum = threadNum;
        }
    }

    public abstract void run();

    public abstract void handleList();
}
