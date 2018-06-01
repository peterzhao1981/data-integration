package com.mode.checkProduct.htmlparse;

import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import com.mode.checkProduct.commoninfo.Common;

/*
 * @author maxiaodong on 2018/05/10
 * @version 0.0.1 检查CheckTmll
 */
public class CheckTmll extends AbstractCrawler {

    public CheckTmll(String domainStr, Document document, String result) {
        super(domainStr, document, result);
    }

    public String process() {
        return super.process();
    }

    @Override
    public boolean isOutOfStack() {
        // TODO 需要处理由JS生成的动态数据，后面需要处理
        try {
            Elements elements = document.getElementsByClass("tb-btn-buy tb-btn-sku");
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
        if (checkByTagName("title", "出错啦")) {
            result = Common.RES_PRODUCT_INVALID;
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean isMainPage() {
        if (checkByTagName("title", "天猫tmall.com--理想生活上天猫")) {
            result = Common.RES_PRODUCT_INVALID;
            return true;
        } else {
            return false;
        }
    }

    public static void main(String[] args) {

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
