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
        if (isError404()) {
            return result;
        } else if (isMainPage()) {
            return result;
        } else if (!isOutOfStack()) {
            result = Common.RES_PRODUCT_EXIST;
            return result;
        } else {
            result = Common.HTML_CHANGED;
            return result;
        }
    }

    // 无货
    public abstract boolean isOutOfStack();

    // Error404
    public abstract boolean isError404();

    // 是否跳转到主页
    public abstract boolean isMainPage();

    // 判断是否error的通用方法，判断tagname是否含有某些关键信息
    public boolean checkByTagName(String tagName, String errorMSG) {
        boolean flag = false;
        Elements elements = document.getElementsByTag(tagName);
        for (Element element : elements) {
            if (element.ownText().contains(errorMSG)) {
                flag = true;
                result = Common.RES_PRODUCT_INVALID;
                break;
            }
        }
        return flag;
    }

}
