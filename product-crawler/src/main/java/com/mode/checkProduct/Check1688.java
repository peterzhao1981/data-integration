package com.mode.checkProduct;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Connection;
import org.jsoup.Connection.Method;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/*
 * @author maxiaodong on 2018/05/09
 * @version 0.0.1
 */
public class Check1688 extends AbstractCrawler {
    private static final String LOGIN1688_URL = "https://login.taobao.com/member/login.jhtml?style=b2b&css_style=b2b&from=b2b&newMini2=true&full_redirect=true&redirect_url=https%3A%2F%2Flogin.1688.com%2Fmember%2Fjump.htm%3Ftarget%3Dhttps%253A%252F%252Flogin.1688.com%252Fmember%252FmarketSigninJump.htm%253FDone%253Dhttps%25253A%25252F%25252Fre.1688.com%25252F%25253Ftrackid%25253D8856887723658105284138%252526cosite%25253Dbaidujj%252526keywordid%25253D69816439683%252526format%25253Dnormal";
    private String url = null;
    private Connection conn = null;

    public static Map<String, String> cookieMap = null;

    public Check1688(String domainStr, Document document, String result, String url) {
        super(domainStr, document, result);
        this.url = url;
        dealForbidden();
    }

    public Check1688(String domainStr, Document document, String result) {
        super(domainStr, document, result);
    }

    // 重新登录，设置登录的cookie
    @Override
    protected void dealForbidden() {
        Map<String, String> cookieMap = new HashMap<>();
        cookieMap = login();

        conn = Jsoup.connect(url);
        conn.headers(Common.requestHeader());
        conn.cookies(cookieMap);
        try {
            document = conn.get();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    // 处理1688
    public String process() {
        // 判断是不是快照页面
        System.out.println("开始处理1688");
        if (is1688QuickPhoto()) {
            changeDocumentFromNewUrl();
        }
        System.out.println(document.body());
        if (domainStr.contains("detail")) {
            processDetail1688();
        } else if (domainStr.contains("trade")) {
            processTrade1688();
        }
        return result;
    }

    private void processTrade1688() {
        // todo 需要加一个快照生成的网页，需要继续进入
        // https://trade.1688.com/order/offer_snapshot.htm?spm=a360q.8274423.1130995625.48.1MiDSd&buyer_id=2000324232&order_entry_id=82511636546323242
        processDetail1688();// 暂时发现这两个网页一样，所以先调同一个方法
    }

    private String processDetail1688() {
        if (isForbidden()) {
            // 如果被禁止爬虫了，需要重新进行处理，设置代理
            result = Common.HTML_FORBIDDEN;
            // dealForbidden();
            if (isForbidden()) {
                result = Common.HTML_FORBIDDEN;
                return result;
            }
        }
        if (isError404()) {
            return result;
        }
        if (isMainPage()) {
            return result;
        }
        if (!isOutOfStack()) {
            result = Common.RES_PRODUCT_EXIST;
            return result;
        }
        if (isProductPage(getindexNum(url))) {
            return result = Common.RES_PRODUCT_EXIST;
        }
        // else 最终处理结果
        result = Common.RES_PRODUCT_INVALID;
        System.out.println("maybe 禁止爬虫");
        return result;
    }

    // 判断是否包含trade 1688的快照页面，如果包含需要重新进入url
    private boolean is1688QuickPhoto() {
        boolean flag = false;
        Elements elements = document.getElementsByClass("sub-logo-v4");
        for (Element element : elements) {
            if (element.ownText() == "快照") {
                flag = true;
            }
        }
        return flag;
    }

    // 如果是1688快照页面，需要获取到新的URL
    private String getNewUrlFrom1688QuickPhoto() {
        String newUrl = null;
        Elements elements = document.getElementsByClass("button button-large");
        for (Element element : elements) {
            newUrl = element.attr("href");
        }
        return newUrl;
    }

    // 得到1688新的url后需要更新docunment，进行爬取。
    private void changeDocumentFromNewUrl() {
        String newUrl = getNewUrlFrom1688QuickPhoto();
        Connection conn = Jsoup.connect(newUrl);
        conn.headers(Common.requestHeader());
        conn.ignoreContentType(true);
        try {
            document = conn.get();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            System.out.println("从快照页面，更新docunment失败");
        }
    }

    // 读取价格的方法，返回价格list列表。
    public List<BigDecimal> getPriceListFrom1688() {
        // 得到包含价格的顶级div
        List<BigDecimal> priceList = new ArrayList<>();
        // 先判断是不是快照页面，如果是的话，需要更新document
        if (is1688QuickPhoto()) {
            changeDocumentFromNewUrl();
        }
        Elements elements = document.getElementsByClass("mod-detail-price");
        for (Element element : elements) {
            Elements elements2 = element.getElementsByAttributeValueContaining("class",
                    "value price-length");
            for (Element element2 : elements2) {
                priceList.add(new BigDecimal(element2.ownText()));
            }
        }
        return priceList;
    }

    @Override
    public boolean isOutOfStack() {
        // TODO Auto-generated method stub
        return false;
    }

    // 手机端app返回信息
    @Override
    public boolean isError404() {
        if (checkByClassName("info", "该商品无法查看或已下架")) {
            result = Common.RES_PRODUCT_INVALID;
            return true;
        }
        if (checkByTagName("title", "404-阿里巴巴")) {
            result = Common.RES_PRODUCT_INVALID;
            return true;
        }
        return false;
    }

    // pc端页面
    public boolean isError404PC() {
        // TODO Auto-generated method stub
        boolean flag = false;
        Elements elements = document.select("h3");
        if (elements != null) {
            for (Element element : elements) {
                // 这里我爬取的是error页面，如果有这个页面则说明商品不存在
                if (element.hasClass("title")) {
                    result = Common.RES_PRODUCT_INVALID;
                    flag = true;
                    break;// 如果已经404Error了直接退出循环
                }
            }
        }

        Elements elements2 = document.select("b");
        if (elements2 != null) {
            for (Element element : elements2) {
                if (element.ownText().contains("对不起，暂时没有符合您要求的信息")) {
                    result = Common.RES_PRODUCT_INVALID;
                    flag = true;
                    break;// 如果已经404Error了直接退出循环
                }
            }
        }

        return flag;
    }

    // 1688不会进入主页
    @Override
    public boolean isMainPage() {
        if (checkByTagName("title", "阿里巴巴1688.com - 全球领先的采购批发平台,批发网")) {
            result = Common.RES_PRODUCT_INVALID;// 表示PC端首页
            return true;
        }

        if (checkByTagName("title", "阿里巴巴1688.com，天下好货，一手掌握")) {
            result = Common.RES_PRODUCT_INVALID;
            return true;// 表示手机端首页
        }
        return false;
    }

    @Override
    protected boolean isForbidden() {
        // TODO Auto-generated method stub
        if (checkById("msgTip", "1688/淘宝会员（仅限会员名）请在此登录")) {
            result = Common.HTML_FORBIDDEN;
            return true;
        }
        if (checkByClassName("ph-label", "会员名/邮箱/手机号")) {
            result = Common.HTML_FORBIDDEN;
            return true;
        }
        if (checkByTagName("p", "发现您的网络环境有异常")) {
            result = Common.HTML_FORBIDDEN;
            return true;
        }
        return false;
    }

    // 模拟登陆1688
    public Map<String, String> login() {
        Connection connection = Jsoup.connect(LOGIN1688_URL);
        Map<String, String> headerMap = new HashMap<>();
        headerMap = Common.requestHeader();

        connection.headers(headerMap);
        try {
            Response response = connection.execute();
            Document document1 = Jsoup.parse(response.body());// 转换为dom树
            List<Element> et = document1.select("#J_Form");// 获取form表单，可以通过查看页面源码代码得知
            Map<String, String> datas = new HashMap<>();
            for (Element e : et.get(0).getAllElements()) {
                if (e.attr("name").equals("TPL_username")) {
                    e.attr("value", "z_w_w1981");// 设置用户名
                }
                if (e.attr("name").equals("TPL_password")) {
                    e.attr("value", "T3ciT3ci"); // 设置用户密码
                }
                if (e.attr("name").length() > 0) {// 排除空值表单属性
                    datas.put(e.attr("name"), e.attr("value"));
                }
            }
            /**
             * 第二次请求，post表单数据，以及cookie信息
             * 
             **/
            Connection con2 = Jsoup.connect(Check1688.LOGIN1688_URL);
            con2.headers(headerMap);
            // 设置cookie和post上面的map数据
            Response login = con2.ignoreContentType(true).method(Method.POST).data(datas)
                    .cookies(response.cookies()).execute();
            // 打印，登陆成功后的信息
            // System.out.println(login.body());
            // 登陆成功后的cookie信息，可以保存到本地，以后登陆时，只需一次登陆即可
            cookieMap = login.cookies();
            for (String s : cookieMap.keySet()) {
                System.out.println(s + "      " + cookieMap.get(s));
            }

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return cookieMap;
    }

    private boolean isProductPage(String indexNum) {
        List<Element> et = document.getElementsByTag("meta");
        for (Element element : et) {
            if (element.attr("content").matches(indexNum)) {
                return true;// 表示是产品页面
            }
        }
        return false;
    }

    // 得到URl的商品索引编号
    private String getindexNum(String url) {
        String regexStr = "[^//]*?\\.(html?)";
        Pattern pattern = Pattern.compile(regexStr, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(url.trim().replaceAll("：", ":").replaceAll(" ", ""));
        if (matcher.find()) {
            return matcher.group().replace(".html", "").trim();
        } else {
            // url有问题，直接跳过爬取，将status设置为"error url"
            System.out.println("url有问题，请检查url，或者正则表达式代码");
            return Common.URL_ERROR;
        }
    }

    public static void main(String[] args) {
        String url = "http://www.bao69.com/product-616110.html";
        Document document = null;
        Connection connection = Jsoup.connect(url);
        try {
            document = connection.get();
            System.out.println(document);
        } catch (Exception e) {
            byte[] aaa = new byte[0];
            synchronized (aaa) {
                System.out.println(connection.response().statusCode());
                e.printStackTrace();
                System.out.println(document);
            }
        } finally {
            System.out.println("aa" + connection.response().statusCode());// 0
        }

    }

}
