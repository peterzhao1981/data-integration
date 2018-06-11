package com.mode.checkProduct.threadcontrol;

import java.util.List;

import com.mode.checkProduct.commoninfo.Common;

/*
 * @author maxiaodong on 2018/05/17
 * @version 0.0.1
 * 启动其他网页爬取的多线程
 */
public class ThreadOtherTask extends AbstractThreadControl {

    public ThreadOtherTask(List<String> dataList, int threadNum) {
        super(dataList, threadNum);
    }

    public ThreadOtherTask(String threadName, List<String> dataList, int i, int length) {
        super(threadName, dataList, i, length);
    }

    @Override
    public void run() {
        // TODO 这里处理数据,设置dataList以及list的起始位置
        List<String> subList = dataList.subList(start, end);
        Common common = new Common();
        String url = null;
        String orignUrl = null;
        for (String entity : subList) {
            // 去掉URL中错误的字符，这一步很重要
            try {
                orignUrl = entity;
                url = Common.getRightURL(orignUrl);
            } catch (Exception e) {
                e.printStackTrace();
                url = "null";
            } finally {
                common.process(url, orignUrl);
            }
        }
        System.out.println(threadName + "处理了" + subList.size() + "条！");
        threadCountTask.endGateCountDown();
    }

    /**
     * 多线程处理list
     *
     * @param data
     *            数据list
     * 
     * @param threadNum
     *            线程数
     */
    @Override
    public synchronized void handleList() {
        int length = dataList.size();
        // int tl = length % threadNum == 0 ? length / threadNum : (length /
        // threadNum + 1);
        int tl = length / threadNum;
        for (int i = 0; i < threadNum; i++) {
            String threadName = "其他商品线程:" + i;
            int end = (i + 1) * tl;
            ThreadOtherTask task = null;
            if (i == threadNum - 1) {
                task = new ThreadOtherTask(threadName, dataList, i * tl, length);
            } else {
                task = new ThreadOtherTask(threadName, dataList, i * tl, end);
            }
            Thread thread = new Thread(task);
            thread.start();
        }
    }

}
