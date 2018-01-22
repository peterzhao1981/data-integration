package com.mode.service;

import org.springframework.web.bind.annotation.CrossOrigin;

/**
 * Created by zhaoweiwei on 2017/12/21.
 */
public class Test {

    public static void main(String[] args) {
//        int i = 1;
//        int j = 1;
//        for (int x = 1; x <= 10; x ++) {
//            for (i = 1; i <= 36; i ++) {
//                for (j = 1; j <= 12; j ++) {
//                    System.out.format("%02d-%02d%n", i, j);
//                }
//            }
//        }
        String value = "1#";
        if (value.endsWith("#")) {
            value = value.substring(0, value.length() - 1);
            System.out.println(value);
        }

    }
}
