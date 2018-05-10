package com.mode.checkProduct;

import org.jsoup.nodes.Document;

public class CheckTaobao extends AbstractCrawler {

    public CheckTaobao(String domainStr, Document document, String result) {
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
        // TODO Auto-generated method stub
        return false;
    }

}
