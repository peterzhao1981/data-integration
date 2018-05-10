package com.mode.checkProduct;

import java.io.IOException;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/*
 * @author maxiaodong on 2018/05/10
 * @version 0.0.1 检查Xunyix
 */
public class CheckXunyix extends AbstractCrawler {

    public CheckXunyix(String domainStr, Document document, String result) {
        super(domainStr, document, result);
        // TODO Auto-generated constructor stub
    }

    @Override
    public boolean isOutOfStack() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean isError404() {
        return checkByTagName("h1", "您访问的页面地址有误");// 注意这个是contains方式
    }

    @Override
    public boolean isMainPage() {
        // TODO 待修改
        boolean flag = false;
        Elements elements = document.getElementsByClass("j-banner-nav-box-title");
        for (Element element : elements) {
            String ownText = element.ownText();
            if (ownText.contains("百货") || ownText.contains("百货")) {
                flag = true;
                result = Common.RES_PRODUCT_INVALID;
                break;
            }
        }
        return flag;
    }

    public static void main(String[] args) {
        String url = "http://www.xunyix.com/goods/3487752.html";
        Document document = null;
        Connection connection = Jsoup.connect(url);
        try {
            document = connection.get();
            CheckXunyix ss = new CheckXunyix(null, document, null);
            System.err.println(ss.isMainPage());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            System.out.println("aa" + connection.response().statusCode());// 0
        }
    }
}
