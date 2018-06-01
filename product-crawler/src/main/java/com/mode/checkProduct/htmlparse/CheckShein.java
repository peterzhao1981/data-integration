package com.mode.checkProduct.htmlparse;

import java.util.List;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import com.mode.checkProduct.commoninfo.Common;

public class CheckShein extends AbstractCrawler {

    public CheckShein(String domainStr, Document document, String result) {
        super(domainStr, document, result);
        // TODO Auto-generated constructor stub
    }

    public static void main(String[] args) throws Exception {
        String domainStr = "www.shein.com";
        String result = null;
        String url = "http://www.shein.com/Crochet-Insert-Backless-Tassel-Tie-Pom-Pom-Cover-Up-p-359318-cat-1866.html";
        Document document = null;
        Connection connection = Jsoup.connect(url);
        connection.headers(Common.requestHeader());
        connection.timeout(5000);
        document = connection.get();
        CheckShein checkHaoduoyi = new CheckShein(domainStr, document, result);
        checkHaoduoyi.process();
        System.out.println(checkHaoduoyi.result);
    }

    @Override
    protected void dealForbidden() {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean isOutOfStack() {
        List<Element> elements = document.getElementsByClass("she-btn-black she-btn-xl");
        if (elements != null) {
            for (Element element : elements) {
                if (element.attr("@click") != null
                        && element.attr("@click").contains("addToBag($event)"))
                    return false;
            }
        }
        return true;
    }

    @Override
    public boolean isError404() {

        return false;
    }

    @Override
    public boolean isMainPage() {
        if (checkByTagName("title", "SheIn.com"))
            return true;
        if (checkByTagName("title", "SHEIN Spring Sale"))
            return true;
        return false;
    }

    @Override
    protected boolean isForbidden() {
        // TODO Auto-generated method stub
        return false;
    }

}
