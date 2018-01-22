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
public class TaoBaoHttpUtils {
    private static final Logger LOG = LoggerFactory.getLogger(TaoBaoHttpUtils.class);

    public static String getProductName(String url) {
        Document doc;
        try {
            doc = Jsoup.connect(url).get();
            try {
                Thread.currentThread().sleep(2000);
            } catch (Exception e) {
            }
            Elements elements = doc.getElementsByAttributeValue("class", "tb-main-title");
            if (!elements.isEmpty()) {
                for (Element element : elements) {
                    if ("h3".equals(element.tagName())) {
                        return element.attr("data-title");
                    }
                }
            }

        } catch (Exception e) {
            LOG.error("Error processing -> " + url);
        }
        return null;
    }

    public static void main(String[] args) {
        System.out.println(TaoBaoHttpUtils.getProductName("https://item.taobao.com/item.htm?spm=a1z09.2.0.0.ec574f8xn1V0C&id=40101929710&_u=f5tqgr5e4a7"));
    }
}
