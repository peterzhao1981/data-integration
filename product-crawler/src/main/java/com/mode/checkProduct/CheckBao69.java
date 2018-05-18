package com.mode.checkProduct;

import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class CheckBao69 extends AbstractCrawler {

    public CheckBao69(String domainStr, Document document, String result) {
        super(domainStr, document, result);
        // TODO Auto-generated constructor stub
    }

    @Override
    public boolean isOutOfStack() {
        // TODO Auto-generated method stub
        try {
            Elements elements = document
                    .getElementsByClass("btn btn-import btn-huge action-buynow");
            if (elements.get(0).text().contains("立即购买")) {
                return false;
            }
            return true;
        } catch (Exception e) {
            return true;
        }
    }

    @Override
    public boolean isError404() {
        if (document == null) {
            result = Common.RES_PRODUCT_INVALID;
            return true;
        }
        return false;
    }

    @Override
    public boolean isMainPage() {
        if (checkByTagName("title", "包69商城")) {
            result = Common.RES_PRODUCT_INVALID;
            return true;
        }
        return false;
    }

    @Override
    protected boolean isForbidden() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    protected void dealForbidden() {
        // TODO Auto-generated method stub

    }

}
