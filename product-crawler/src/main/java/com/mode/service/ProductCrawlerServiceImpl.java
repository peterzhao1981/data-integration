package com.mode.service;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mode.entity.Product;
import com.mode.repository.ProductRepository;
import com.mode.util.SheinHttpUtils;
import com.mode.util.WhatsmodeHttpsUtils;
import com.mode.util.ZafulHttpUtils;

/**
 * Created by zhaoweiwei on 2017/11/10.
 */
@Service
public class ProductCrawlerServiceImpl {

    @Autowired
    private ProductRepository productRepository;

    public void initProducts() {
        List<Product> products = ZafulHttpUtils.initProducts();
        for (Product product : products) {
            productRepository.save(product);
        }
    }


    public void processZaful() {
        List<Product> products = productRepository.findByStatus("ready", null);
        for (Product product : products) {
            String url = product.getUrl();
            String doman = getDomain(url);
            product.setDomain(doman);
            product.setCurrency("USD");
            product = ZafulHttpUtils.getProduct(product);
            if (product != null) {
                product.setStatus("done");
                productRepository.save(product);
            }
        }
    }

    public void processShein() {
        List<Product> products = SheinHttpUtils.getProducts();
        for (Product product : products) {
            productRepository.save(product);
        }
    }

    public void processWhatsmode() {
        List<Product> products = WhatsmodeHttpsUtils.getProducts();
        for (Product product : products) {
            productRepository.save(product);
        }
    }


    private String getDomain(String url) {
        try {
            Pattern p = Pattern.compile("[^//]*?\\.(com|cn|net|org|biz|info|cc|tv)",
                    Pattern.CASE_INSENSITIVE);
            Matcher matcher = p.matcher(url);
            matcher.find();
            return matcher.group();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    public static void main(String[] args) {
        try{
            String url = "https://www.zaful.com/embroidered-loose-high-low-sweater-p_234773.html";
            Pattern p = Pattern.compile("(?<=http(s)?://|\\.)[^.]*?\\.(com|cn|net|org|biz|info|cc|tv)",Pattern.CASE_INSENSITIVE);
            Matcher matcher = p.matcher(url);
            matcher.find();
            System.out.println("域名是："+ matcher.group());

            p = Pattern.compile("[^//]*?\\.(com|cn|net|org|biz|info|cc|tv)", Pattern.CASE_INSENSITIVE);
            matcher = p.matcher(url);
            matcher.find();
            System.out.println("完整的域名是："+matcher.group());
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }
}
