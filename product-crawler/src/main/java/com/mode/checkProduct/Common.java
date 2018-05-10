package com.mode.checkProduct;

import java.io.IOException;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
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

    public final static String HTML_CHANGED = "html structe changed";// 网页结构变化,或者网页错误
    public final static String UNKONWN_HTML = "unknown html";// 新网页，需要重新分析
    public final static String HTML_UNKNOWN_REASON = "no reason";// 莫名其妙的原因，导致错误，可能是代码逻辑错误

    private Check1688 check1688 = null;

    @Autowired
    private CheckProductStatusRepository checkProductRepository;

    public void process(String domainStr, String url, Long id) {
        Document docunment = null;
        Connection conn = null;
        String result = null;
        try {
            result = Common.RES_CONNECT_TIME_OUT;
            conn = Jsoup.connect(url)
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
                    // TODO ,异常代码，后续需要处理诶
                    if (conn.response().statusCode() == 0) {
                        result = RES_CONNECT_TIME_OUT;// 连接超时，无法连接该网页
                    } else if (conn.response().statusCode() == 200) {
                        result = UNKONWN_HTML;// 说明是新的供应商，需要加一个爬虫类,
                    } else {
                        result = HTML_UNKNOWN_REASON;// 莫名其妙的原因，可能是代码逻辑错误
                    }

                    System.out.println(result + "无法爬取网页诶");
                }

                checkProductRepository.setCheckProductStatusStatus(result, id);

            } else {
                checkProductRepository.setCheckProductStatusStatus(result, id);
            }
        }

    }

    public static Document getDocument(String url) throws Exception {
        Connection conn = Jsoup.connect(url)
                .userAgent("Mozilla/4.0 (compatible; MSIE 9.0; Windows NT 6.1; Trident/5.0")
                .header("Connection", "close").ignoreContentType(true);
        Document document = conn.get();
        return document;

    }

    public static void main(String[] args) {

    }

}
