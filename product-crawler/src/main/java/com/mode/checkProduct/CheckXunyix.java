package com.mode.checkProduct;

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
        if (checkByTagName("h1", "您访问的页面地址有误")) {
            result = Common.RES_PRODUCT_INVALID;
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean isMainPage() {
        Elements elements = document.getElementsByClass("j-banner-nav-box-title");
        for (Element element : elements) {
            for (Element element1 : element.getElementsByTag("a")) {
                String ownText = element1.ownText();
                if (ownText.contains("百货") || ownText.contains("平台货源") || ownText.contains("美妆")) {
                    result = Common.RES_PRODUCT_INVALID;
                    return true;
                }
            }

        }
        return false;
    }

    public static void main(String[] args) {

    }

    @Override
    protected boolean isForbidden() {
        return false;
    }

    @Override
    protected void dealForbidden() {

    }
}
