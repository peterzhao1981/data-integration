package com.mode.checkProduct;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class CheckTaobao extends AbstractCrawler {
    private boolean isDetailTaobao = true;

    public CheckTaobao(String domainStr, Document document, String result) {
        super(domainStr, document, result);
        if (domainStr.contains("item")) {// 国内淘宝网
            isDetailTaobao = true;
        } else if (domainStr.contains("world")) {
            isDetailTaobao = false;
        } else {
            isDetailTaobao = true;
        }
    }

    @Override
    public boolean isOutOfStack() {
        try {
            Elements elements = document.getElementsByClass("tb-btn-buy");
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
        if (isDetailTaobao == true) {
            if (checkByClassName("error-notice-hd", "很抱歉，您查看的宝贝不存在")) {
                result = Common.RES_PRODUCT_INVALID;
                return true;
            }
            if (checkByTagName("strong", "此宝贝已下架")) {
                result = Common.RES_PRODUCT_INVALID;
                return true;
            }
            return false;
        } else {
            return false;
        }
    }

    @Override
    public boolean isMainPage() {
        if (isDetailTaobao == true) {
            if (checkByTagName("title", "淘宝网 - 淘！我喜欢") || checkByTagName("title", "爱逛街")) {
                result = Common.RES_PRODUCT_INVALID;
                return true;
            }
            return false;
        } else {
            return false;
        }
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

    public static void main(String[] args) throws Exception {
        Connection connection = Jsoup
                .connect("http://cn.memebox.com/catalog/product/view/id/23627");
        Document document = null;
        document = connection.get();
        CheckMemebox checkMemebox = new CheckMemebox("cn.memebox.com", document, null);
        String result = checkMemebox.process();
        System.out.println(result);
    }

}
