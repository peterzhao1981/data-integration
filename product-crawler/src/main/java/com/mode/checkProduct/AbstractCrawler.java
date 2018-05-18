package com.mode.checkProduct;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public abstract class AbstractCrawler {
    public String domainStr = null;
    public Document document = null;
    public String result = null;

    public AbstractCrawler(String domainStr, Document document, String result) {
        this.domainStr = domainStr;
        this.document = document;
        this.result = result;
    }

    // 处理的主方法
    public String process() {
        if (isForbidden()) {
            // 如果被禁止爬虫了，需要重新进行处理，设置代理
            result = Common.HTML_FORBIDDEN;
            dealForbidden();
        }
        if (isError404()) {
            return result;
        }
        if (isMainPage()) {
            return result;
        }
        // 如果不缺货，说明商品存在。默认isOutOfStack=false
        if (!isOutOfStack()) {
            result = Common.RES_PRODUCT_EXIST;
            return result;
        } else {
            result = Common.RES_PRODUCT_LACK;
            return result;
        }
    }

    // 处理反爬虫方法
    protected abstract void dealForbidden();

    // 无货
    public abstract boolean isOutOfStack();

    // Error404
    public abstract boolean isError404();

    // 是否跳转到主页
    public abstract boolean isMainPage();

    // 判断是否被禁止爬虫了
    protected abstract boolean isForbidden();

    // 如果被禁止爬虫了，需要进行相关处理处理，设置代理或者模拟登陆
    protected void changeCrawlerWay() {

    }

    // 判断是否error的通用方法，判断tagname是否含有某些关键信息
    protected boolean checkByTagName(String tagName, String errorMSG) {
        boolean flag = false;
        Elements elements = document.getElementsByTag(tagName);
        if (elements != null) {
            for (Element element : elements) {
                if (element != null && element.text().contains(errorMSG)) {
                    flag = true;
                    return flag;
                }
            }
        }
        return flag;
    }

    // 判断是否error的通用方法，判断className是否含有某些关键信息
    protected boolean checkByClassName(String className, String errorMSG) {
        boolean flag = false;
        Elements elements = document.getElementsByClass(className);
        if (elements != null) {
            for (Element element : elements) {
                if (element != null && element.text().contains(errorMSG)) {
                    flag = true;
                    return flag;
                }
            }
        }

        return flag;
    }

    // 判断是否error的通用方法，判断id是否含有某些关键信息
    protected boolean checkById(String id, String errorMSG) {
        boolean flag = false;
        Element element = document.getElementById(id);
        if (element != null && element.text().contains(errorMSG)) {
            flag = true;
            return flag;
        }
        return flag;
    }

    // 判断error的通用方法，判断className是否含有某些关键信息，这里使用其他方法
    protected boolean checkByClassName(String className, String errorMSG, int checkFlag) {
        boolean flag = false;
        Elements elements = document.getElementsByClass(className);
        if (elements != null) {
            for (Element element : elements) {
                if (element != null && element.ownText().contains(errorMSG)) {
                    flag = true;
                    return flag;
                }
            }
        }
        return flag;
    }

}
