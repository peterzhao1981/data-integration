package com.mode.checkProduct.htmlparse;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.mode.checkProduct.commoninfo.Common;

public class CheckAmazon extends AbstractCrawler {

    public CheckAmazon(String domainStr, Document document, String result) {
        super(domainStr, document, result);
        // TODO Auto-generated constructor stub
    }

    @Override
    protected void dealForbidden() {
        // TODO Auto-generated method stub
    }

    @Override
    public boolean isOutOfStack() {
        // TODO Auto-generated method stub
        if (checkById("submit.add-to-cart-announce", "Add to Cart"))
            return false;
        return true;
    }

    @Override
    public boolean isError404() {
        if (checkByTagName("title", "Page Not Found"))
            return true;
        return false;
    }

    @Override
    public boolean isMainPage() {
        if (checkByTagName("title", "Amazon.com:"))
            return true;
        return false;
    }

    @Override
    protected boolean isForbidden() {
        // TODO Auto-generated method stub
        return false;
    }

    public static void main(String[] args) throws Exception {
        String domainStr = "cn.memebox";
        String result = null;
        String url = "http://cn.memebox.com/catalog/product/view/id/14106";
        Document document = null;
        Connection connection = Jsoup.connect(url);
        connection.headers(Common.requestHeader());
        connection.timeout(5000);
        document = connection.get();
        CheckMemebox checkHaoduoyi = new CheckMemebox(domainStr, document, result);
        checkHaoduoyi.process();
        System.out.println(checkHaoduoyi.result);
    }

}
