package com.mode.checkProduct;

import java.io.IOException;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/*
 * @author maxiaodong on 2018/05/10
 * @version 0.0.1 检查Haoduoyi
 */
public class CheckHaoduoyi extends AbstractCrawler {

    public CheckHaoduoyi(String domainStr, Document document, String result) {
        super(domainStr, document, result);
        // TODO Auto-generated constructor stub
    }

    public String process() {
        return super.process();
    }

    @Override
    public boolean isOutOfStack() {
        // TODO Auto-generated method stub
        if (checkByClassName("btnbuy buy_now", "立即购买")) {
            return false;
        }
        return true;
    }

    @Override
    public boolean isError404() {
        if (checkByTagName("p", "规格已下架")) {
            result = Common.RES_PRODUCT_LACK;
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean isMainPage() {
        String metaContentAttr = "好多衣haoduoyi.com - 跨境出口女装供应链:快时尚女装、童装、内衣、大码女装专业供应商。";
        Elements elements = document.getElementsByTag("meta");
        for (Element element : elements) {
            String content = element.attr("content");
            if (content.equals(metaContentAttr)) {
                result = Common.RES_PRODUCT_INVALID;
                return true;
            }
        }
        return false;
    }

    public static void main(String[] args) throws IOException {
        String domainStr = "www.haoduoyi.com";
        String result = null;
        String url = "http://www.haoduoyi.com/index.php?c=index&m=shop&id=26433&gid=34263";
        Document document = null;
        Connection connection = Jsoup.connect(url);
        document = connection.get();
        CheckHaoduoyi checkHaoduoyi = new CheckHaoduoyi(domainStr, document, result);
        checkHaoduoyi.process();
        System.out.println(checkHaoduoyi.result);

        // String url =
        // "http://www.haoduoyi.com/index.php?c=index&m=shop&id=374&gid=5301";
        // Document document = null;
        // Connection connection = Jsoup.connect(url);
        // try {
        // document = connection.get();
        // CheckHaoduoyi ss = new CheckHaoduoyi(null, document, null);
        // System.out.println(ss.isMainPage());
        //
        // System.out.println(connection.response().url());
        // } catch (IOException e) {
        // e.printStackTrace();
        // } finally {
        // System.out.println("aa" + connection.response().statusCode());// 0
        // }
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
