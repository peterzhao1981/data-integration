package com.mode.service;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.mode.util.RawDataUtil;

/**
 * Created by zhaoweiwei on 2017/11/30.
 */
@Service
public class TestService {

    public TestService() {
        System.out.println("Constructor");
    }

    @PostConstruct
    public void mothed() {
        System.out.println("PostConstruct");
    }

    public void test() {
        System.out.println("test");
    }

    private List<String> list1 = new ArrayList<>();
    private List<String> list2 = new ArrayList<>();

    private void load() {
        RawDataUtil.processLine("test1.txt", line -> {
            if (StringUtils.isEmpty(line)) {
                return;
            }
            list1.add(line);
        });
        System.out.println(list1.size());
        RawDataUtil.processLine("test2.txt", line -> {
            if (StringUtils.isEmpty(line)) {
                return;
            }
            list2.add(line);
        });
        System.out.println(list2.size());
    }

    private void run() {
        for (String s : list1) {
           if (!list2.contains(s)) {
               System.out.println(s);
           }
        }
    }

    public static void main(String[] args) {
        TestService t = new TestService();
        t.load();
        t.run();
    }
}
