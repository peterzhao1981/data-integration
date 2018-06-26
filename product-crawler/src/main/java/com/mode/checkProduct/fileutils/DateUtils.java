package com.mode.checkProduct.fileutils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {
    public static String getCurrentTime() {
        Date date = new Date();
        try {
            Thread.currentThread();
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return String.valueOf(date.getTime()).substring(7);
    }

    public static String getDateYMD() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
        return simpleDateFormat.format(new Date());
    }

    public static void main(String[] args) {
        // TODO Auto-generated method stub
        int i = 0;
        try {
            i = 1 / 0;
            i = 1;
        } catch (Exception e) {
            i = 2;
        }
        System.out.println(i);

        System.out.println(getCurrentTime());

    }

}
