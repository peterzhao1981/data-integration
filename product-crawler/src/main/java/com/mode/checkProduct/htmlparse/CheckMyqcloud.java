package com.mode.checkProduct.htmlparse;

import org.jsoup.nodes.Document;

//此网页无法打开
public class CheckMyqcloud extends AbstractCrawler {

    public CheckMyqcloud(String domainStr, Document document, String result) {
        super(domainStr, document, result);
        // TODO Auto-generated constructor stub
    }

    public static void main(String[] args) {
        // TODO Auto-generated method stub

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
        // TODO Auto-generated method stub
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
