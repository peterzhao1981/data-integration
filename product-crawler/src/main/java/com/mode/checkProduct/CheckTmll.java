package com.mode.checkProduct;

import org.jsoup.nodes.Document;

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
        return false;
    }

    @Override
    public boolean isError404() {
        return checkByTagName("title", "出错啦！");
    }

    @Override
    public boolean isMainPage() {
        // TODO Auto-generated method stub
        return checkByTagName("title", "天猫tmall.com--理想生活上天猫");
    }

    public static void main(String[] args) {

    }

}
