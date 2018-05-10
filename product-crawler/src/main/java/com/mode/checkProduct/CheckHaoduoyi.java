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
        return false;
    }

    @Override
    public boolean isError404() {
        return checkByTagName("p", "规格已下架");
    }

    @Override
    public boolean isMainPage() {
        boolean flag = false;
        String metaContentAttr = "好多衣haoduoyi.com - 跨境出口女装供应链:快时尚女装、童装、内衣、大码女装专业供应商。";
        Elements elements = document.getElementsByTag("meta");
        for (Element element : elements) {
            String content = element.attr("content");
            if (content.equals(metaContentAttr)) {
                flag = true;
                result = Common.RES_PRODUCT_INVALID;
                break;
            }
        }
        return flag;
    }

    public static void main(String[] args) {
        String url = "http://www.haoduoyi.com/index.php?c=index&m=shop&id=374&gid=5301";
        Document document = null;
        Connection connection = Jsoup.connect(url);
        try {
            document = connection.get();
            CheckHaoduoyi ss = new CheckHaoduoyi(null, document, null);
            System.out.println(ss.isMainPage());

            System.out.println(connection.response().url());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            System.out.println("aa" + connection.response().statusCode());// 0
        }
    }

}
