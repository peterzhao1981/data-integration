package com.mode.checkProduct;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.PostConstruct;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mode.checkProduct.commoninfo.ConfigInfo;
import com.mode.checkProduct.getinstance.GetInstance;
import com.mode.ippool.htmlparse.URLFecter;
import com.mode.ippool.ipfilter.IPUtils;
import com.mode.ippool.ipmodel.IPMessage;
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

    // 重载上一个方法，处理非1688的网页，爬虫方式验证
    public void process(String url, Long id) {
        Document document = null;
        Connection conn = null;
        String result = Common.URL_ERROR;
        String domainStr = null;
        Map<String, String> requsetHeader = new HashMap<>();
        requsetHeader = Common.requestHeader();
        try {
            // 先判断url格式正确与否，注意只判断为空，若要完整判断，使用正则表达式比较费时间
            if (isURL(url)) {
                conn = Jsoup.connect(url);
                conn.headers(requsetHeader);
                conn.timeout(3000);
                document = conn.get();
                domainStr = Common.getDomainStr(url);
                GetInstance getInstance = new GetInstance(result, document, domainStr);
                result = getInstance.getResult();
            } else {
                result = Common.URL_ERROR;
            }
        } catch (Exception e) {
            System.out.println("错误id：" + id);
            result = RES_CONNECT_TIME_OUT;// url连接超时，无法打开该网页
            e.printStackTrace();
        } finally {
            System.out.println("finally:" + result + ";" + id);
            commonUtil.checkProductRepository.updateStatus(result, id);
        }
    }

    // 重载上一个方法，处理1688商城信息，调用API方式，需要传入int类型
    public void process(String url, Long id, int flag) {
        String result = null;
        // 进行处理之前首先要验证url格式的正确性，注意验证的必要性，否则会导致线程阻塞
        if (isURL(url)) {
            Check1688API check1688 = new Check1688API(url);
            result = check1688.process();
            // TODO 可能有问题
            // 如果为达到了API调用的上限，则需要切换key与secret的值
            if (result.equals(Common.API_MAX) && ConfigInfo.appIndex < ConfigInfo.appArrLen) {
                ++ConfigInfo.appIndex;
                ConfigInfo.appKey = ConfigInfo.appKeyArr[ConfigInfo.appIndex];
                ConfigInfo.appSecret = ConfigInfo.appSecretArr[ConfigInfo.appIndex];
            }
        } else {
            result = Common.URL_ERROR;
        }
        commonUtil.checkProductRepository.updateStatus(result, id);
        System.out.println(result + ";" + id);
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

    // 设置爬虫暂停随机时间
    public static int randomSleepTime(int minTime, int maxTime) {
        Random random = new Random();
        int time = random.nextInt(maxTime - minTime + 1);
        return time + minTime;
    }

    // 判断ip是否可用，如果不可用则继续下一个，如果循环完毕，都不可用，则进行ip地址爬虫
    public static IPMessage getVaildIP(String lastIP) {
        // 得到代理
        List<IPMessage> ipMessageslist = new ArrayList<>();
        URLFecter.urlParseNN(ipMessageslist, "http://www.xicidaili.com/nn/1");

        for (IPMessage ipMessage2 : ipMessageslist) {
            if (ipMessage2.getIPAddress() == lastIP) {
                continue;
            }
            if (IPUtils.IPIsable(ipMessage2)) {
                return ipMessage2;
            }
        }
        return ipMessageslist.get(0);
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
            System.out.println("url有问题：" + url);
            return Common.URL_ERROR;
        }
    }

    // 得到1688商城的url链接中间的index编号
    public static String getProductID(String productUrl) {
        String regexStr = "[^//]*?\\.(html?)";
        String matchResult = null;
        Pattern pattern = Pattern.compile(regexStr, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern
                .matcher(productUrl.replaceAll("：", ":").replaceAll(" ", "").trim());
        if (Common.isURL(productUrl) && matcher.find()) {
            matchResult = matcher.group().replace(".html", "").trim();
            regexStr = ".*[\\D]+.*";// 判断包含非数字吗
            matcher = Pattern.compile(regexStr).matcher(matchResult);
            return matcher.matches() || matchResult.length() == 0 ? Common.URL_ERROR : matchResult;
        } else {
            // url有问题，直接跳过爬取，将status设置为"error url"
            System.out.println("url有问题，请检查url，没有商品id");
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

    // 下面的test方法是测试多线程使用
    public volatile static int count = 0;

    private synchronized void test(int i) {
        String url4 = "http://cn.memebox.com/catalog/product/view/id/11953";
        Connection connection = Jsoup.connect(url4);
        connection.timeout(3000);
        try {
            Document document = connection.get();
            if (document == null) {
                System.out.println(document);
            }
        } catch (Exception e) {
            // e.printStackTrace();
        } finally {
            count++;
            System.out.println(
                    Thread.currentThread().getName() + ";" + i + ";" + "链接超时" + ";count=" + count);
        }
    }

    public static void main(String[] args) throws IOException {
        Document document = null;
        Connection connection = Jsoup.connect("http://cn.memebox.com/pony/05-25437");

        try {
            document = connection.get();
            System.out.println(document);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(document);
            System.out.println(connection.response().statusCode());
        }

    }

    public static void main1() {
        String url1 = "https://blog.csdn.net/yongh701/article/details/46894417";
        String url2 = "http://blog.csdn.net/yongh701/article/details/46894417";
        String url3 = "=VLOOKUP(A103,'[1]New Product Excel'!$E$2:$AF$130,20,FALSE)";
        String url4 = "https://www.youtube.com/";
        System.out.println(isURL(url1) + ";" + isURL(url2) + ";" + isURL(url3) + ";" + isURL(url4));
        Connection connection = Jsoup.connect(url4);
        for (int i = 0; i < 100; i++) {
            Thread thread = new Thread() {
                public void run() {
                    for (int j = 0; j < 5; j++) {
                        Common common = new Common();
                        common.test(j);
                    }
                }
            };
            thread.start();
        }

    }

}
