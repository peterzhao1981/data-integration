package com.mode.checkProduct;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/*
 * @author maxiaodong on 2018/05/09
 * @version 0.0.1
 */
public class Check1688 {

    private String domainStr = null;
    private Document docunment = null;
    private String result = null;

    public Check1688(String domainStr, Document docunment, String result) {
        this.domainStr = domainStr;
        this.docunment = docunment;
        this.result = result;
    }

    // 处理detail.1688.com
    public String process() {
        if (domainStr.contains("detail")) {
            processDetail1688();
        } else if (domainStr.contains("trade")) {

        }

        return result;
    }

    public void processTrade1688() {
        processDetail1688();// 暂时发现这两个网页一样，所以先调同一个方法
    }

    public void processDetail1688() {
        Elements elements = docunment.select("h3");
        for (Element element : elements) {
            // 这里我爬取的是error页面，如果有这个页面则说明商品不存在
            if (element.hasClass("title")) {
                result = Common.RES_PRODUCT_INVALID;
                break;// 如果已经404Error了直接退出循环
            }
        }
        Elements elements2 = docunment.select("b");
        for (Element element : elements2) {
            if (element.ownText().contains("对不起，暂时没有符合您要求的信息")) {
                result = Common.RES_PRODUCT_INVALID;
            }
        }
    }

    public static void main(String[] args) {
        Check1688 check1688 = new Check1688("aa", null, null);
        System.out.println(check1688.result);
        check1688.processTrade1688();
        System.out.println(check1688.result);

    }

}
