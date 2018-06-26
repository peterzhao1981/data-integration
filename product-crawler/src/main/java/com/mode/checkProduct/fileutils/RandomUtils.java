package com.mode.checkProduct.fileutils;

public class RandomUtils {
    // 根据时间生成随机数
    public static String getRandomByTime() {
        int randomLong = (int) (Math.random() * 1000);
        return DateUtils.getDateYMD() + "_" + DateUtils.getCurrentTime()
                + String.valueOf(randomLong);
    }

    public static void main(String[] args) {
        // TODO Auto-generated method stub
        System.out.println(getRandomByTime());
    }

}
