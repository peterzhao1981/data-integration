package com.mode.checkProduct;

import java.io.IOException;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;

import com.mode.repository.CheckProductStatusRepository;

/*
 * @author maxiaodong on 2018/05/09
 * @version 0.0.1
 */
public class Common {
    // 爬虫网页返回值信息
    public final static String RES_CONNECT_TIME_OUT = "connect time out";// 网站连接超时
    public final static String RES_PRODUCT_EXIST = "product exist";// 商品存在
    public final static String RES_PRODUCT_INVALID = "product invalid";// 商品不存在
    public final static String RES_PRODUCT_LACK = "product lack";// 商品缺货

    public final static String UNKONWN_HTML = "unknown html";// 新网页，需要重新分析

    Check1688 check1688 = null;

    @Autowired
    private CheckProductStatusRepository checkProductRepository;

    public void process(String domainStr, String url, Long id) {
        Document docunment = null;
        String result = null;
        try {
            result = Common.RES_CONNECT_TIME_OUT;
            Connection conn = Jsoup.connect(url)
                    .userAgent("Mozilla/4.0 (compatible; MSIE 9.0; Windows NT 6.1; Trident/5.0")
                    .header("Connection", "close").ignoreContentType(true);
            docunment = conn.get();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (docunment != null) {
                result = Common.RES_PRODUCT_EXIST;
                if (domainStr.contains("1688")) {
                    check1688 = new Check1688(domainStr, docunment, result);
                    result = check1688.process();
                } else if (domainStr.contains("dear-lover")) {

                } else if (domainStr.contains("tmall")) {

                } else if (domainStr.contains("taobao")) {

                } else if (domainStr.contains("haoduoyi")) {

                } else if (domainStr.contains("xunyix")) {

                } else if (domainStr.contains("kmeila")) {

                } else if (domainStr.contains("memebox ")) {

                } else if (domainStr.contains("myqcloud")) {

                } else {
                    // To do ,后续需要处理诶
                    result = UNKONWN_HTML;// 说明是新的供应商，需要加一个爬虫类
                    System.out.println(UNKONWN_HTML + "这个网页我以前没遇到，需要加一个新类");
                }

                checkProductRepository.setCheckProductStatusStatus(result, id);

            } else {
                checkProductRepository.setCheckProductStatusStatus(result, id);
            }

        }
    }

    public static void main(String[] args) {
        Document docunment = null;
        String result = null;
        String[] urlArr = {
                "https://trade.1688.com/order/offer_snapshot.htm?spm=a360q.8728381.content.95.6oYweh" };
        for (String url : urlArr) {
            try {
                result = "connect time out";
                Connection conn = Jsoup.connect(url)
                        .userAgent("Mozilla/4.0 (compatible; MSIE 9.0; Windows NT 6.1; Trident/5.0")
                        .header("Connection", "close").ignoreContentType(true);

                docunment = conn.post();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (docunment != null) {
                    Elements elements = docunment.select("h3");
                    for (Element element : elements) {
                        if (element.hasClass("title")) {
                            System.out.println("error");
                        }
                    }
                    Elements elements2 = docunment.select("b");
                    for (Element element : elements2) {
                        if (element.textNodes().get(0).toString().contains("对不起，暂时没有符合您要求的信息")) {
                            result = Common.RES_PRODUCT_INVALID;
                        }
                    }
                    System.out.println(result);
                } else {
                    System.out.println(result);
                }

            }
        }
    }

}
