package com.mode.util;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mode.entity.Product;

/**
 * Created by zhaoweiwei on 2017/11/13.
 */
public class ZafulHttpUtils {

    private static final Logger LOG = LoggerFactory.getLogger(ZafulHttpUtils.class);

    private static final String USER_AGENT = "Mozilla/5.0 Firefox/26.0";

    private static final int TIMEOUT_SECONDS = 120;

    private static final int POOL_SIZE = 120;

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static List<Product> initProducts() {
        Document doc;
        long now = System.currentTimeMillis();
        List<Product> products = new ArrayList<>();
        String baseUrl = "https://www.zaful.com/clothes-e_1/";
        for (int i = 1; i <= 167; i ++) {
            String url = baseUrl + "g_" + i + ".html";
            try {
                System.out.println(url);
                doc = Jsoup.connect(url).get();
                try {
                    Thread.currentThread().sleep(2000);
                } catch (Exception e) {
                }

                Elements productElements = doc.getElementsByAttributeValue("class", "pic js_exposure");
                for (Element element : productElements) {
                    if (element.hasAttr("href")) {
                        String productUrl = element.attr("href");
                        if (!StringUtils.isEmpty(productUrl)) {
                            Product product = new Product();
                            product.setUrl(productUrl);
                            products.add(product);
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Error processing -> " + url);
            }
        }
        return products;
    }

    public static Product getProduct(Product product) {
        Document doc;
        String title = null;
        String listPrice = null;
        String salePrice = null;
        String coverImage = null;
        Integer reviewCount = 0;

        long now = System.currentTimeMillis();
        try {
            doc = Jsoup.connect(product.getUrl()).get();
            try {
                Thread.currentThread().sleep(1000);
            } catch (Exception e) {
            }

            Elements titleElement = doc.getElementsByTag("h1");
            if (!titleElement.isEmpty()) {
                title = titleElement.get(0).text();
            }

            Elements priceElement = doc.getElementsByAttributeValue("class", "shop-price my_shop_price");
            if (!priceElement.isEmpty()) {
                if (priceElement.get(0).hasAttr("data-orgp")) {
                    salePrice = priceElement.get(0).attr("data-orgp");
                }
            }

            priceElement = doc.getElementsByAttributeValue("class", "market-price");
            if (!priceElement.isEmpty()) {
                Elements strongElements = priceElement.get(0).getElementsByTag("strong");
                if (!strongElements.isEmpty()) {
                    if (strongElements.get(0).hasAttr("data-orgp")) {
                        listPrice = strongElements.get(0).attr("data-orgp");
                    }
                }
            }
            if (StringUtils.isEmpty(listPrice)) {
                listPrice = salePrice;
            }

            Elements imageElements = doc.getElementsByAttributeValue("class", "active");
            if (!imageElements.isEmpty()) {
                if (imageElements.get(0).hasAttr("data-big-img")) {
                    coverImage = imageElements.get(0).attr("data-big-img");
                }
            }

            Elements reviewsElements = doc.getElementsByAttributeValue("class", "reviews");
            if (!reviewsElements.isEmpty()) {
                Elements strongElements = reviewsElements.get(0).getElementsByTag("strong");
                if (!strongElements.isEmpty()) {
                    String reviews = strongElements.get(0).text();
                    if (!StringUtils.isEmpty(reviews)) {
                        reviewCount = Integer.parseInt(reviews);
                    }
                }
            }

            product.setTitle(title);
            product.setListPrice(listPrice);
            product.setSalePrice(salePrice);
            product.setCoverImageUrl(coverImage);
            product.setReviewCount(reviewCount);

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error processing -> " + product.getId());
            return null;
        }
        return product;
    }

    public static void main(String[] args) {
        Document doc;
        String title = null;
        String price = null;
        long now = System.currentTimeMillis();
        String baseUrl = "https://www.zaful.com/clothes-e_1/";
        for (int i = 1; i <= 167; i ++) {
            String url = "https://www.zaful.com/clothes-e_1/" + "g_" + i + ".html";
            try {
                System.out.println(url);
                doc = Jsoup.connect(url).get();
                try {
                    Thread.currentThread().sleep(2000);
//                    System.out.println(doc);
                } catch (Exception e) {
                }

                Elements productElements = doc.getElementsByAttributeValue("class", "pic js_exposure");
                for (Element element : productElements) {
                    if (element.hasAttr("href")) {
                        System.out.println(element.attr("href"));
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
//        try {
//            doc = Jsoup.connect(url).get();
//            try {
//                Thread.currentThread().sleep(2000);
//                System.out.println(doc);
//            } catch (Exception e) {
//            }
//
//            Elements productElements = doc.getElementsByAttributeValue("class", "pic js_exposure");
//            for (Element element : productElements) {
//                if (element.hasAttr("href")) {
//                    System.out.println(element.attr("href"));
//                }
//            }
//            Elements titleElement = doc.getElementsByTag("h1");
//            System.out.println(titleElement.get(0).text());
//            Elements priceElement = doc.getElementsByAttributeValue("class", "shop-price my_shop_price");
//            if (!priceElement.isEmpty()) {
//                if (priceElement.get(0).hasAttr("data-orgp")) {
//                    price = priceElement.get(0).attr("data-orgp");
//                    System.out.println(price);
//                }
//            }
//
//            Elements priceElements2 = doc.getElementsByAttributeValue("class", "market-price");
//            if (!priceElements2.isEmpty()) {
//                Elements strongElements = priceElements2.get(0).getElementsByTag("strong");
//                if (!strongElements.isEmpty()) {
//                    if (strongElements.get(0).hasAttr("data-orgp")) {
//                        price = strongElements.get(0).attr("data-orgp");
//                        System.out.println(price);
//                    }
//                }
//            }
//            String picture = null;
//            Elements picutreElements = doc.getElementsByAttributeValue("class", "active");
//            if (!picutreElements.isEmpty()) {
//                if (picutreElements.get(0).hasAttr("data-big-img")) {
//                    picture = picutreElements.get(0).attr("data-big-img");
//                    System.out.println(picture);
//                }
//            }
//
//            String reviews = null;
//            Elements reviewsElements = doc.getElementsByAttributeValue("class", "reviews");
//            if (!reviewsElements.isEmpty()) {
//                Elements strongElements = reviewsElements.get(0).getElementsByTag("strong");
//                if (!strongElements.isEmpty()) {
//                    reviews = strongElements.get(0).text();
//                    System.out.println(reviews);
//                }
//            }

//        } catch (Exception e) {
//            e.printStackTrace();
//        }

    }

}
