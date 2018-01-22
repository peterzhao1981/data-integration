package com.mode.service;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Service;

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
}
