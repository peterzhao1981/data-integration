package com.mode.checkProduct.commoninfo;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.PostConstruct;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mode.checkProduct.getinstance.GetInstance;
import com.mode.checkProduct.htmlparse.Check1688API;
import com.mode.repository.CheckProductStatusRepository;

/*
 * @author maxiaodong on 2018/05/18
 * @version 0.0.1
 */
@Component
public class Common {
    // 爬虫网页返回值信息
    public final static String RES_CONNECT_TIME_OUT = "connect time out";// 网站连接超时
    public final static String RES_PRODUCT_EXIST = "product exist";// 商品存在
    public final static String RES_PRODUCT_INVALID = "product invalid";// 商品不存在
    public final static String RES_PRODUCT_LACK = "product lack";// 商品缺货

    public final static String HTML_CHANGED = "html structe changed";// 网页结构变化,或者网页错误
    public final static String UNKONWN_HTML = "unknown html";// 新网页，需要重新分析
    public final static String HTML_UNKNOWN_REASON = "no reason";// 莫名其妙的原因，导致错误，可能是代码逻辑错误
    public final static String URL_ERROR = "error url";
    public final static String HTML_FORBIDDEN = "frobid";
    public final static String API_MAX = "api max";// 已经达到API最大请求次数

    @Autowired
    private CheckProductStatusRepository checkProductRepository;

    // 设置自动注入
    public static Common commonUtil;

    @PostConstruct
    public void init() {
        commonUtil = this;
        commonUtil.checkProductRepository = this.checkProductRepository;
    }

    // 处理非1688的网页，爬虫方式验证
    public void process(String url, String orignUrl) {
        Document document = null;
        Connection conn = null;
        String result = null;
        String domainStr = null;
        Map<String, String> requsetHeader = new HashMap<>();
        requsetHeader = Common.requestHeader();
        try {
            // 先判断url格式正确与否，注意只判断为空，若要完整判断，使用正则表达式比较费时间
            if (isURL(url)) {
                conn = Jsoup.connect(url);
                conn.headers(requsetHeader);
                conn.timeout(5000);
                document = conn.get();
                domainStr = Common.getDomainStr(url);
                GetInstance getInstance = new GetInstance(result, document, domainStr);
                getInstance.returnInstance();
                result = getInstance.getResult();
            } else {
                result = Common.URL_ERROR;
            }
            commonUtil.checkProductRepository.updateInfo(result, null, orignUrl);
        } catch (Exception e) {
            result = Common.URL_ERROR;// url连接超时，无法打开该网页
            commonUtil.checkProductRepository.updateInfo(result, null, orignUrl);
            System.out.println("非1688" + result + orignUrl);
        }
    }

    // 重载上一个方法，处理1688商城信息，调用API方式，需要传入int类型
    public void process(String url, String orignUrl, int flag) {
        String result = null;
        String shortSize = "";
        // 进行处理之前首先要验证url格式的正确性，注意验证的必要性，否则会导致线程阻塞
        if (isURL(url)) {
            Check1688API check1688 = new Check1688API(url);
            result = check1688.process()[0];// url的status
            shortSize = check1688.process()[1];// 缺货信息
            // 如果为达到了API调用的上限，则需要切换key与secret的值
            try {
                if (result != null && result.equals(Common.API_MAX)
                        && ConfigInfo.appIndex < ConfigInfo.appArrLen - 1) {
                    ++ConfigInfo.appIndex;
                    ConfigInfo.appKey = ConfigInfo.appKeyArr[ConfigInfo.appIndex];
                    ConfigInfo.appSecret = ConfigInfo.appSecretArr[ConfigInfo.appIndex];
                }
            } finally {
            }
        } else {
            result = Common.URL_ERROR;
            shortSize = "";
        }
        commonUtil.checkProductRepository.updateInfo(result, shortSize, orignUrl);
        System.out.println(result + "," + shortSize + ";" + orignUrl);
    }

    // 返回请求头，使用随机方式生成请求头，防止反爬虫
    public static Map<String, String> requestHeader() {
        Map<String, String> headerMap = new HashMap<>();
        Random random = new Random();
        int index = 0;

        String[] arrAgent = ConfigInfo.arrAgent;
        index = random.nextInt(arrAgent.length - 1);

        headerMap.put("User-Agent", arrAgent[index]);
        headerMap.put("Connection", "close");
        headerMap.put("Accept",
                "text/html,application/xhtml+xml,application/xml;" + "q=0.9,image/webp,*/*;q=0.8");
        headerMap.put("Accept-Language", "zh-CN,zh;q=0.8");
        headerMap.put("Accept-Encoding", "gzip, deflate, sdch");
        headerMap.put("Pragma", "no-cache");
        return headerMap;
    }

    // 返回docunment信息，注意try-catch的使用，需要注意在多线程中的必要性。如果异常不处理会导致线程阻塞。
    // 如果catch中出现异常，也会导致线程阻塞
    public static Document getDocument(String url) throws Exception {
        Connection conn = Jsoup.connect(url);
        conn.headers(Common.requestHeader());
        try {
            Document document = conn.get();
            return document;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // 得到product_url的域名，用于判断需要使用哪一种爬虫方法，抓取网页.使用该方法之前需要将url的异常字符去掉
    public static String getDomainStr(String url) {
        String regexStr = "[^//]*?\\.(com|cn|net|org|biz|info|cc|tv)";
        Pattern pattern = Pattern.compile(regexStr, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(url.trim().replaceAll("：", ":").replaceAll(" ", ""));
        if (Common.isURL(url) && matcher.find()) {
            return matcher.group();
        } else {
            // url有问题，直接跳过爬取，将status设置为"error url"
            return Common.URL_ERROR;
        }
    }

    // 得到1688商城的url链接中间的index编号
    public static String getProductID(String productUrl) {
        String regexStr = "[^/]*?\\.html?";
        String matchResult = null;
        Pattern pattern = Pattern.compile(regexStr, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(getRightURL(productUrl));
        if (!productUrl.contains("trade.1688") && Common.isURL(productUrl) && matcher.find()) {
            matchResult = matcher.group();
            matchResult = matchResult.split("\\.")[0];
            regexStr = ".*[\\D]+.*";// 判断包含非数字吗
            matcher = Pattern.compile(regexStr).matcher(matchResult);
            return matcher.matches() || matchResult.length() == 0 ? Common.URL_ERROR : matchResult;
        } else {
            // 如果是1688tradde快照页面，需要从快照页面提取index
            if (productUrl.contains("trade.1688"))
                return Common.getTrade1688ProductID(productUrl);
            return Common.URL_ERROR;
        }
    }

    // 判断是否为url地址，由于判断方式比较复杂，而且效率低所以只判断为空即可。
    public static boolean isURL(String url) {
        // 若传入的就是null类型，将会导致使用string判断空字符串方法失败，报空指针异常，阻塞线程
        if (url == null) {
            return false;
        }
        if (url.isEmpty()) {
            return false;
        }
        if (url.trim().length() <= 0) {
            return false;
        }
        if (url.equals("null")) {
            return false;
        }
        return true;
    }

    /*
     * 将url中的不规则字符去掉
     */
    public static String getRightURL(String url) {
        if (isURL(url)) {
            return url.replaceAll("：", ":").replaceAll(" ", "").trim();
        } else {
            return "null";
        }
    }

    /*
     * 如果是trade1688，则可能是快照页面，或者是无效的1688页面
     */
    public static String getTrade1688ProductID(String productUrl) {
        try {
            Document document = Common.getDocument(productUrl);
            Elements elements = document.getElementsByClass("button button-large");
            for (Element element : elements) {
                if (element != null)
                    return getProductID(element.attr("href"));// 回调
            }
            return Common.URL_ERROR;
        } catch (Exception e) {
            e.printStackTrace();
            return Common.URL_ERROR;
        }

    }

    public static void main(String[] args) {
        System.out.println(Common.getProductID(
                "https://detail.1688.com/offer/554829312140.html?spm=a2615.7691456.0.0.35fe109flEFxDM"));
    }
}
