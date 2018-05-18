package com.mode.checkProduct.threadcontrol;

import java.util.List;

import com.mode.checkProduct.Common;
import com.mode.entity.CheckProductStatus;

/*
 * @author maxiaodong on 2018/05/17
 * @version 0.0.1
 * 启动1688网页爬取的多线程
 */
public class Thread1688Task extends Thread implements Runnable {
    private List<CheckProductStatus> dataList;
    private int start;
    private int end;
    private int threadNum;
    private String threadName;

    public Thread1688Task(String threadName, List<CheckProductStatus> dataList, int start,
            int end) {
        this.threadName = threadName;
        this.dataList = dataList;
        this.start = start;
        this.end = end;
    }

    public Thread1688Task(List<CheckProductStatus> dataList, int threadNum) {
        this.dataList = dataList;
        this.threadNum = threadNum;
    }

    @Override
    public void run() {
        // TODO 这里处理数据,设置dataList以及list的起始位置
        List<CheckProductStatus> subList = dataList.subList(start, end);
        Common common = new Common();
        String url = null;
        for (CheckProductStatus entity : subList) {
            // 去掉URL中错误的字符
            try {
                url = entity.getProductUrl().toString();
                url = url.replaceAll("：", ":").replaceAll(" ", "").trim();
            } catch (Exception e) {
                e.printStackTrace();
                url = null;
            } finally {
                common.process(url, entity.getId(), 1688);
                System.out.println(threadName + "正在处理");
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

    public synchronized void handleList() {
        int length = dataList.size();
        int tl = length % threadNum == 0 ? length / threadNum : (length / threadNum + 1);

        for (int i = 0; i < threadNum; i++) {
            String threadName = "1688处理线程" + i;
            int end = (i + 1) * tl;
            Thread1688Task task = new Thread1688Task(threadName, dataList, i * tl,
                    end > length ? length : end);
            Thread thread = new Thread(task);
            thread.start();
        }
    }

}
