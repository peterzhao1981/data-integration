package com.mode.checkProduct.getinstance;

import org.jsoup.nodes.Document;

import com.mode.checkProduct.commoninfo.Common;
import com.mode.checkProduct.htmlparse.CheckAmazon;
import com.mode.checkProduct.htmlparse.CheckBao69;
import com.mode.checkProduct.htmlparse.CheckDearLover;
import com.mode.checkProduct.htmlparse.CheckHaoduoyi;
import com.mode.checkProduct.htmlparse.CheckKmeila;
import com.mode.checkProduct.htmlparse.CheckMemebox;
import com.mode.checkProduct.htmlparse.CheckMyqcloud;
import com.mode.checkProduct.htmlparse.CheckShein;
import com.mode.checkProduct.htmlparse.CheckTaobao;
import com.mode.checkProduct.htmlparse.CheckTmll;
import com.mode.checkProduct.htmlparse.CheckXunyix;

public class GetInstance {
    private String result = null;
    private Document document = null;
    private String domainStr = null;

    private CheckDearLover checkDearLover = null;
    private CheckHaoduoyi checkHaoduoyi = null;
    private CheckXunyix checkXunyix = null;
    private CheckBao69 checkBao69 = null;
    private CheckKmeila checkKmeila = null;
    private CheckMemebox checkMemebox = null;
    private CheckTmll checkTmll = null;
    private CheckTaobao checkTaobao = null;
    private CheckMyqcloud checkMyqcloud = null;
    private CheckAmazon checkAmazon = null;
    private CheckShein checkShein = null;

    public GetInstance(String result, Document document, String domainStr) {
        this.result = result;
        this.document = document;
        this.domainStr = domainStr;
    }

    public void returnInstance() {
        if (domainStr.contains("dear-lover")) {
            checkDearLover = new CheckDearLover(domainStr, document, result);
            result = checkDearLover.process();
        } else if (domainStr.contains("tmall")) {
            checkTmll = new CheckTmll(domainStr, document, result);
            result = checkTmll.process();
        } else if (domainStr.contains("taobao")) {
            checkTaobao = new CheckTaobao(domainStr, document, result);
            result = checkTaobao.process();
        } else if (domainStr.contains("haoduoyi")) {
            checkHaoduoyi = new CheckHaoduoyi(domainStr, document, result);
            result = checkHaoduoyi.process();
        } else if (domainStr.contains("xunyix")) {
            checkXunyix = new CheckXunyix(domainStr, document, result);
            result = checkXunyix.process();
        } else if (domainStr.contains("kmeila")) {
            checkKmeila = new CheckKmeila(domainStr, document, result);
            result = checkKmeila.process();
        } else if (domainStr.contains("memebox")) {
            checkMemebox = new CheckMemebox(domainStr, document, result);
            result = checkMemebox.process();
        } else if (domainStr.contains("myqcloud")) {
            checkMyqcloud = new CheckMyqcloud(domainStr, document, result);
            result = checkMyqcloud.process();
        } else if (domainStr.contains("bao69")) {
            checkBao69 = new CheckBao69(domainStr, document, result);
            result = checkBao69.process();
        } else if (domainStr.contains("amazon")) {
            checkAmazon = new CheckAmazon(domainStr, document, result);
            result = checkAmazon.process();
        } else if (domainStr.contains("shein")) {
            checkShein = new CheckShein(domainStr, document, result);
            result = checkShein.process();
        } else {
            result = Common.UNKONWN_HTML;// 说明是未知的网页，需要新增代码解析
        }
    }

    public String getResult() {
        return result;
    }

}
