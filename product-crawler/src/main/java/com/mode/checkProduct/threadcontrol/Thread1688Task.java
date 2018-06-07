package com.mode.checkProduct.threadcontrol;

import java.util.List;

import com.mode.checkProduct.commoninfo.Common;

/*
 * @author maxiaodong on 2018/05/17
 * @version 0.0.1
 * 启动1688网页爬取的多线程
 */
public class Thread1688Task extends AbstractThreadControl {

    public Thread1688Task(String threadName, List<String> dataList, int start, int end) {
        super(threadName, dataList, start, end);
    }

    public Thread1688Task(List<String> dataList, int threadNum) {
        super(dataList, threadNum);
    }

    @Override
    public void run() {
        // TODO 这里处理数据,设置dataList以及list的起始位置
        List<String> subList = dataList.subList(start, end);
        Common common = new Common();
        String url = null;
        String orignUrl = null;
        for (String entity : subList) {
            // 去掉URL中错误的字符
            try {
                orignUrl = entity;
                url = Common.getRightURL(orignUrl);
            } catch (Exception e) {
                e.printStackTrace();
                url = "null";
            } finally {
                // common.process(url, entity.getId(), 1688);
                System.out.println("1688:" + Thread.currentThread().getName());
                common.process(url, orignUrl, 1688);
            }
        }
        System.out.println(threadName + "处理了" + subList.size() + "条！");
        Thread.currentThread().interrupt();
    }

    /**
     * 多线程处理list
     *
     * @param data
     *            数据list
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
            String threadName = "1688处理线程" + i;
            int end = (i + 1) * tl;
            Thread1688Task task = null;
            if (i == threadNum - 1) {
                task = new Thread1688Task(threadName, dataList, i * tl, length);
            } else {
                task = new Thread1688Task(threadName, dataList, i * tl, end);
            }
            Thread thread = new Thread(task);
            thread.start();
        }
    }

}
