package com.mode.checkProduct.htmlparse;

import org.jsoup.nodes.Document;

/*
 * 如果是whatsMode的产品，暂时不处理，默认有货
 */
public class CheckWhatsMode extends AbstractCrawler {

    public CheckWhatsMode(String domainStr, Document document, String result) {
        super(domainStr, document, result);
        // TODO Auto-generated constructor stub
    }

    public static void main(String[] args) {
        // TODO Auto-generated method stub
    }

    @Override
    protected void dealForbidden() {
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

}
