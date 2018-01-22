package com.mode.util;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.yaml.snakeyaml.extensions.compactnotation.PackageCompactConstructor;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mode.entity.Product;
import com.mode.entity.SheinProduct;
import com.mode.entity.SheinProductList;

/**
 * Created by zhaoweiwei on 2017/11/20.
 */
public class WhatsmodeHttpsUtils {

    private static final Logger LOG = LoggerFactory.getLogger(WhatsmodeHttpsUtils.class);

    public static List<Product> getProducts() {
        Document doc;
        long now = System.currentTimeMillis();
        List<Product> products = new ArrayList<>();
        String baseUrl = "https://whatsmode.com/collections/nicole?page=";
        for (int i = 1; i <= 16; i ++) {
            String url = baseUrl + i;
            try {
                doc = Jsoup.connect(url).validateTLSCertificates(false).get();
                try {
                    Thread.currentThread().sleep(2000);
                } catch (Exception e) {
                }
                Elements productLoopElements = doc.getElementsByAttributeValue("id", "product-loop");
                if (!productLoopElements.isEmpty()) {
                    Element productLoopElement = productLoopElements.get(0);
                    Elements productDetailsElements = productLoopElement.getElementsByAttributeValue("class",
                            "product-details");
                    for (Element productDetailsElement : productDetailsElements) {
                        String title = null;
                        String price = null;
                        String salePrice = null;

                        Elements aElements = productDetailsElement.getElementsByTag("a");
                        if (!aElements.isEmpty()) {
                            title = aElements.get(0).text();
                        }
                        Elements prodPriceElements = productDetailsElement.getElementsByAttributeValue("class", "prod-price");
                        if (!prodPriceElements.isEmpty()) {
                            price = prodPriceElements.get(0).text();
                        } else {
                            Elements onSaleElements = productDetailsElement.getElementsByAttributeValue("class",
                                    "onsale");
                            if (!onSaleElements.isEmpty()) {
                                salePrice = onSaleElements.get(0).text();
                            }
                            Elements wasListingElements = productDetailsElement.getElementsByAttributeValue("class",
                                    "was-listing");
                            if (!wasListingElements.isEmpty()) {
                                price = wasListingElements.get(0).text();
                            }
                        }
                        if (!StringUtils.isEmpty(title)) {
                            System.out.println("title : " + title + " price : " + price + " salePrice : " + salePrice);
                            Product product = new Product();
                            product.setUrl("N/A");
                            product.setTitle(title);
                            product.setDomain("https://www.whatsmode.com/");
                            product.setCurrency("USD");
                            product.setListPrice(price);
                            product.setSalePrice(salePrice);
                            products.add(product);
                        }
                    }
                }

            } catch (Exception e) {
                LOG.error("Error processing -> " + url);
            }
        }
        return products;
    }


    public static void main(String[] args) {
        String url = "https://whatsmode.com/collections/thanksgiving-day?page=93";
        Document doc;
        long now = System.currentTimeMillis();
        try {
            doc = Jsoup.connect(url).validateTLSCertificates(false).get() ;
            System.out.println(doc);

            Elements productLoopElements = doc.getElementsByAttributeValue("id", "product-loop");
            if (!productLoopElements.isEmpty()) {
                Element productLoopElement = productLoopElements.get(0);
                Elements productDetailsElements = productLoopElement.getElementsByAttributeValue("class",
                        "product-details");
                for (Element productDetailsElement : productDetailsElements) {
                    String title = null;
                    String price = null;
                    String salePrice = null;

                    Elements aElements = productDetailsElement.getElementsByTag("a");
                    if (!aElements.isEmpty()) {
                        title = aElements.get(0).text();
                    }
                    Elements prodPriceElements = productDetailsElement.getElementsByAttributeValue("class", "prod-price");
                    if (!prodPriceElements.isEmpty()) {
                        price = prodPriceElements.get(0).text();
                    } else {
                        Elements onSaleElements = productDetailsElement.getElementsByAttributeValue("class",
                                "onsale");
                        if (!onSaleElements.isEmpty()) {
                            salePrice = onSaleElements.get(0).text();
                        }
                        Elements wasListingElements = productDetailsElement.getElementsByAttributeValue("class",
                                "was-listing");
                        if (!wasListingElements.isEmpty()) {
                            price = wasListingElements.get(0).text();
                        }
                    }
                    System.out.println("title : " + title + " price : " + price + " salePrice : " + salePrice);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }



    }
}
