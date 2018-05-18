package com.mode.checkProduct.threadcontrol;

import java.util.List;

import com.mode.checkProduct.Common;
import com.mode.entity.CheckProductStatus;

/*
 * @author maxiaodong on 2018/05/17
 * @version 0.0.1
 * 启动其他网页爬取的多线程
 */
public class ThreadOtherTask extends Thread implements Runnable {
    private List<CheckProductStatus> dataList;
    private int start;
    private int end;
    private String threadName;
    private int threadNum;

    public ThreadOtherTask(String threadName, List<CheckProductStatus> dataList, int start,
            int end) {
        this.threadName = threadName;
        this.dataList = dataList;
        this.start = start;
        this.end = end;
    }

    public ThreadOtherTask(List<CheckProductStatus> dataList, int threadNum) {
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
            // 去掉URL中错误的字符，这一步很重要
            try {
                url = (String) entity.getProductUrl();
                url = url.trim().replaceAll("：", ":").replaceAll(" ", "").trim();
            } catch (Exception e) {
                e.printStackTrace();
                url = null;
            } finally {
                common.process(url, entity.getId());
                System.out.println(threadName + "正在处理");
            }
        }
        System.out.println(threadName + "处理了" + subList.size() + "条！");
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

    public synchronized void handleList() {
        int length = dataList.size();
        int tl = length % threadNum == 0 ? length / threadNum : (length / threadNum + 1);

        for (int i = 0; i < threadNum; i++) {
            String threadName = "其他商品线程:" + i;
            int end = (i + 1) * tl;
            ThreadOtherTask task = new ThreadOtherTask(threadName, dataList, i * tl,
                    end > length ? length : end);
            Thread thread = new Thread(task);
            thread.start();
        }
    }

}
