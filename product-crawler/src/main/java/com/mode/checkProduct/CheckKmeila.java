package com.mode.checkProduct;

import org.jsoup.nodes.Document;

public class CheckKmeila extends AbstractCrawler {

    public CheckKmeila(String domainStr, Document document, String result) {
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
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean isMainPage() {
        if (checkByClassName("current", "首页")) {
            result = Common.RES_PRODUCT_INVALID;
            return true;
        } else {
            return false;
        }
    }

    public static void main(String[] args) {
        // TODO Auto-generated method stub

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
