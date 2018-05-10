package com.mode.checkProduct;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/*
 * @author maxiaodong on 2018/05/09
 * @version 0.0.1
 */
public class Check1688 extends AbstractCrawler {

    public Check1688(String domainStr, Document document, String result) {
        super(domainStr, document, result);
    }

    // 处理1688
    public String process() {
        // 判断是不是快照页面
        if (is1688QuickPhoto()) {
            changeDocumentFromNewUrl();
        }

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

    private void processDetail1688() {
        Elements elements = document.select("h3");
        for (Element element : elements) {
            // 这里我爬取的是error页面，如果有这个页面则说明商品不存在
            if (element.hasClass("title")) {
                result = Common.RES_PRODUCT_INVALID;
                break;// 如果已经404Error了直接退出循环
            }
        }
        Elements elements2 = document.select("b");
        for (Element element : elements2) {
            if (element.ownText().contains("对不起，暂时没有符合您要求的信息")) {
                result = Common.RES_PRODUCT_INVALID;
            }
        }
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
    public void changeDocumentFromNewUrl() {
        String newUrl = getNewUrlFrom1688QuickPhoto();
        Connection conn = Jsoup.connect(newUrl)
                .userAgent("Mozilla/4.0 (compatible; MSIE 9.0; Windows NT 6.1; Trident/5.0")
                .header("Connection", "close").ignoreContentType(true);
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

    public static void main(String[] args) {
        String url = "http://www.xunyix.com/goods/3487752.html";
        Document document = null;
        Connection connection = Jsoup.connect(url);
        try {
            document = connection.get();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            System.out.println("aa" + connection.response().statusCode());// 0
        }
    }

    @Override
    public boolean isOutOfStack() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean isError404() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean isMainPage() {
        // TODO Auto-generated method stub
        return false;
    }

}
