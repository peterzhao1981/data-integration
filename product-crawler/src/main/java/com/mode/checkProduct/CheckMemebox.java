package com.mode.checkProduct;

import org.jsoup.nodes.Document;

public class CheckMemebox extends AbstractCrawler {
    private boolean isCNme = false;// 判断是否是http://cn.memebox.com标志，还有一个网页是us开头

    public CheckMemebox(String domainStr, Document document, String result) {
        super(domainStr, document, result);
        if (domainStr.contains("cn.memebox")) {
            isCNme = true;// 是cn.memebox网页，否则是us网页
        }
    }

    @Override
    public boolean isOutOfStack() {
        if (isCNme) {// 是CN
            if (checkByClassName("availability in-stock", "有货")) {
                result = Common.RES_PRODUCT_EXIST;
                return false;
            }
            result = Common.RES_PRODUCT_LACK;
            return true;
        } else {// 是us
            return false;
        }
    }

    @Override
    public boolean isError404() {
        return false;
    }

    @Override
    public boolean isMainPage() {
        if (isCNme) {// 是CN
            if (checkByTagName("title", "全球知名化妆品平台")) {
                result = Common.RES_PRODUCT_LACK;
                return true;
            }
            return false;
        } else {// 是us
            return false;
        }
    }

    @Override
    protected boolean isForbidden() {
        return false;
    }

    @Override
    protected void dealForbidden() {
    }

}
