package com.mode.checkProduct;

import java.net.InetAddress;
import java.util.List;
import java.util.Map;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class IPSwitch {

    public static void main(String[] args) throws Exception {
        // CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        // HttpPost post = new HttpPost(url);

        Check1688 check1688 = new Check1688("detail.1688.com", null, null);
        // Map<String, String> logincookie = check1688.login();
        Document document = null;

        String url = "https://detail.1688.com/offer/551992057463.html?spm=a2615.7691456.oldlist.70.434b1085V8sS0e";

        // HttpHost proxy = new HttpHost("61.191.41.130", 80);
        // DefaultProxyRoutePlanner routePlanner = new
        // DefaultProxyRoutePlanner(proxy);
        // CloseableHttpClient httpClient =
        // HttpClients.custom().setRoutePlanner(routePlanner).build();
        // HttpPost post = new HttpPost(url);
        // post.addHeader("Content-Type", "application/json");
        // post.addHeader("User-Agent",
        // "Mozilla/5.0 (Windows NT 6.1; rv:6.0.2) Gecko/20100101
        // Firefox/6.0.2");
        // post.addHeader("Accept-Charset", "GB2312,utf-8;q=0.7,*;q=0.7");
        // HttpGet request = new HttpGet(url);
        // CloseableHttpResponse response = httpClient.execute(proxy, post);

        Connection connection = Jsoup.connect(url);
        connection.proxy("114.239.125.14", 61234);
        connection.timeout(5000);
        Map<String, String> requestHeader = Common.requestHeader();
        System.out.println(requestHeader);
        connection.headers(requestHeader);
        // connection.cookies(logincookie);
        try {
            document = connection.get();
        } catch (Exception exception) {
            exception.printStackTrace();
        } finally {
            if (document == null) {
                System.out.println(document);
            } else {
                System.out.println(document);
                List<Element> et = document.getElementsByTag("meta");
                for (Element element : et) {
                    if (element.attr("content").matches("551992057463")) {
                        System.out.println(true);
                    }
                }
            }
            System.out.println(false);
            System.out.println("本机的IP = " + InetAddress.getLocalHost());
        }

    }

}
