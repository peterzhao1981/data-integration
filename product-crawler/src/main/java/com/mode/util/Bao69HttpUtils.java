package com.mode.util;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by zhaoweiwei on 2018/1/15.
 */
public class Bao69HttpUtils {

    private static final Logger LOG = LoggerFactory.getLogger(Bao69HttpUtils.class);

    public static String getProductName(String url) {
        Document doc;
        try {
            doc = Jsoup.connect(url).get();
            try {
                Thread.currentThread().sleep(2000);
            } catch (Exception e) {
            }
            Elements elements = doc.getElementsByAttributeValue("class", "product-titles");
            if (!elements.isEmpty()) {
                for (Element element : elements) {
                    Elements titleElements = element.getElementsByTag("h2");
                    if (!titleElements.isEmpty()) {
                        return titleElements.get(0).text();
                    }
                }

            }

        } catch (Exception e) {
            LOG.error("Error processing -> " + url);
        }
        return null;
    }

    public static void main(String[] args) {
        System.out.println(Bao69HttpUtils.getProductName("http://www.bao69.com/product-5159.html"));
    }
}
