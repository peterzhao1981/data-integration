package com.mode.util;

import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mode.entity.Product;

/**
 * Created by zhaoweiwei on 2018/1/15.
 */
public class Detail1688HttpUtils {

    private static final Logger LOG = LoggerFactory.getLogger(Detail1688HttpUtils.class);

    public static String getProductName(String url) {
        Document doc;
        try {
            doc = Jsoup.connect(url).get();
            try {
                Thread.currentThread().sleep(2000);
            } catch (Exception e) {
            }
            Elements elements = doc.getElementsByAttributeValue("class", "d-title");
            if (!elements.isEmpty()) {
                for (Element element : elements) {
                    if ("h1".equals(element.tagName())) {
                        return element.text();
                    }
                }

            }

        } catch (Exception e) {
            LOG.error("Error processing -> " + url);
        }
        return null;
    }

    public static void main(String[] args) {
        System.out.println(Detail1688HttpUtils.getProductName("https://detail.1688.com/offer/525310773159.html?spm=a2615.7691456.0.0.3df5f33edAkSCZQ"));
    }
}
