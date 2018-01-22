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
public class TmallHttpUtils {

    private static final Logger LOG = LoggerFactory.getLogger(TmallHttpUtils.class);

    public static String getProductName(String url) {
        Document doc;
        try {
            doc = Jsoup.connect(url).get();
            try {
                Thread.currentThread().sleep(2000);
            } catch (Exception e) {
            }
            Elements elements = doc.getElementsByAttributeValue("class", "tb-detail-hd");
            if (!elements.isEmpty()) {
                Elements titleElements = elements.get(0).getElementsByTag("h1");
                if (!titleElements.isEmpty()) {
                    return titleElements.get(0).text();
                }
            }

        } catch (Exception e) {
            LOG.error("Error processing -> " + url);
        }
        return null;
    }

    public static void main(String[] args) {
        System.out.println(TmallHttpUtils.getProductName("https://detail.tmall.com/item.htm?id=557118057279&spm=a1z09.2.0.0.4a039977bxYPU5&_u=u1tgpobk5317&skuId=3617329150689"));
    }
}
