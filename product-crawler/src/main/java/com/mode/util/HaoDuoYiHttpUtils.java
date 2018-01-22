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
public class HaoDuoYiHttpUtils {
    private static final Logger LOG = LoggerFactory.getLogger(HaoDuoYiHttpUtils.class);

    public static String getProductName(String url) {
        Document doc;
        try {
            doc = Jsoup.connect(url).get();
            try {
                Thread.currentThread().sleep(2000);
            } catch (Exception e) {
            }
            Elements elements = doc.getElementsByAttributeValue("class", "productName");
            if (!elements.isEmpty()) {
                return elements.get(0).text();
            }

        } catch (Exception e) {
            LOG.error("Error processing -> " + url);
        }
        return null;
    }

    public static void main(String[] args) {
        System.out.println(HaoDuoYiHttpUtils.getProductName("http://www.haoduoyi.com/index.php?c=index&m=shop&id=13680&gid=17538"));
    }
}
