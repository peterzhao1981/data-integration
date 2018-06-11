package com.mode.checkProduct.htmlparse;

import org.jsoup.nodes.Document;

import com.mode.checkProduct.commoninfo.Common;

/*
 * @author maxiaodong on 2018/05/10
 * @version 0.0.1 检查www.dear-lover.com
 */
public class CheckDearLover extends AbstractCrawler {

    public CheckDearLover(String domainStr, Document document, String result) {
        super(domainStr, document, result);
    }

    public String process() {
        return super.process();// 调用抽象类中的方法，注意该process方法中又调用了抽象类
    }

    @Override
    public boolean isError404() {
        if (checkByTagName("title", "Error 404")) {
            result = Common.RES_PRODUCT_INVALID;
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean isMainPage() {
        if (checkByTagName("title",
                "Wholesale Women's Clothing Online, Cheap Women's Clothes Sale")) {
            result = Common.RES_PRODUCT_INVALID;
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean isOutOfStack() {
        boolean flag = false;
        // to do缺货，不知道如何取得js动态生成的网页
        return flag;
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

        String url = "http://www.dear-lover.com/Khaki-Buttoned-Wrap-Cowl-Neck-Sweater-p24434.html";
        String domainStr = Common.getDomainStr(url);
        Document document = Common.getDocument(url);
        String result = "";
        CheckDearLover checkDearLover = new CheckDearLover(domainStr, document, result);
        System.out.println(checkDearLover.process());
    }

}
