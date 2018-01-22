package com.mode.util;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mode.entity.Product;
import com.mode.entity.SheinProduct;
import com.mode.entity.SheinProductList;

/**
 * Created by zhaoweiwei on 2017/11/14.
 */
public class SheinHttpUtils {

    private static final Logger LOG = LoggerFactory.getLogger(SheinHttpUtils.class);

    public static List<Product> getProducts() {
        Document doc;
        long now = System.currentTimeMillis();
        List<Product> products = new ArrayList<>();
        String baseUrl = "http://us.shein.com/US-AW-Trends-vc-34738-p";
        for (int i = 1; i <= 75; i ++) {
            String url = baseUrl + i + ".html";
            try {
                doc = Jsoup.connect(url).get();
                try {
                    Thread.currentThread().sleep(2000);
                } catch (Exception e) {
                }
                int dataIndex = doc.html().indexOf("var goodsListData=");
                if (dataIndex != -1) {
                    String data = doc.html().substring(dataIndex + 18);
                    dataIndex = data.indexOf("}];");
                    if (dataIndex != -1) {
                        data = data.substring(0, dataIndex + 2);
                        System.out.println(data);
                    }
                    String newData = "{\"sheinProducts\":" + data + "}";

                    ObjectMapper om = new ObjectMapper();
                    om.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                    SheinProductList sheinProducts = om.readValue(newData, SheinProductList.class);
                    if (sheinProducts != null) {
                        for (SheinProduct sheinProduct : sheinProducts.getSheinProducts()) {
                            Product product = new Product();
                            if (!StringUtils.isEmpty(sheinProduct.getUrl())) {
                                product.setTitle(sheinProduct.getGoodsName());
                                product.setUrl("http://us.shein.com/" + sheinProduct.getUrl());
                                product.setCoverImageUrl(sheinProduct.getGoodsThumb());
                                product.setDomain("http://us.shein.com/");
                                product.setCurrency("USD");
                                product.setListPrice(sheinProduct.getShopPrice());
                                product.setSalePrice(sheinProduct.getSpecialPrice());
                                SheinProduct.Comment comment = sheinProduct.getComment();
                                if (comment == null) {
                                    product.setReviewCount(0);
                                } else {
                                    product.setReviewCount(Integer.parseInt(comment.getCommentNum()));
                                }
                                product.setStatus("done");
                                products.add(product);
                            }


                        }
                    }

                }


            } catch (Exception e) {
                LOG.error("Error processing -> " + url);
            }
        }
        return products;
    }


//    public static Product getProduct(Product product) {
//        Document doc;
//        String title = null;
//        String listPrice = null;
//        String salePrice = null;
//        String coverImage = null;
//        Integer reviewCount = 0;
//
//        try {
//            doc = Jsoup.connect(product.getUrl()).get();
//            System.out.println(doc);
//            try {
//                Thread.currentThread().sleep(2000);
//            } catch (Exception e) {
//            }
//            Elements titleElement = doc.getElementsByAttributeValue("class", "name");
//            if (!titleElement.isEmpty()) {
//                title = titleElement.get(0).text();
//                System.out.println(title);
//            }
//
//            int indexShopPrice = doc.html().indexOf("\"shop_price\"");
//            int indexSpecialPrice = doc.html().indexOf("\"special_price\"");
//            // List price
//            String listPriceStr = doc.html().substring(indexShopPrice + 14);
//            int quotes = listPriceStr.indexOf("\"");
//            listPrice = listPriceStr.substring(0, quotes);
//            // Sale price
//            String salePriceStr = doc.html().substring(indexSpecialPrice + 16);
//            int comma = salePriceStr.indexOf(",");
//            if ("0".equals(salePrice)) {
//                salePrice = listPrice;
//            } else {
//                salePrice = salePrice + ".00";
//            }
//            // Cover image
//            Elements imageElements = doc.getElementsByAttributeValue("onload", "imgDetailLoadGa()");
//            if (!imageElements.isEmpty()) {
//                if (imageElements.get(0).hasAttr("src")) {
//                    coverImage = imageElements.get(0).attr("src");
//                }
//            }
//            // Review count
//            Elements reviewsElements = doc.getElementsByAttributeValue("class", "cmtNum");
//            if (!reviewsElements.isEmpty()) {
//                String reviews = reviewsElements.get(0).text();
//                if (!StringUtils.isEmpty(reviews)) {
//                    reviewCount = Integer.parseInt(reviews);
//                }
//            }
//
//            product.setTitle(title);
//            product.setListPrice(listPrice);
//            product.setSalePrice(salePrice);
//            product.setCoverImageUrl(coverImage);
//            product.setReviewCount(reviewCount);
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            System.out.println("Error processing -> " + product.getId());
//            return null;
//        }
//        return product;
//    }


    public static void main(String[] args) {
//        String url = "http://us.shein.com/Low-Back-Scallop-Raw-Edge-Jumper-p-370612-cat-1734.html";

//        String url = "http://us.shein.com/Turtleneck-Ribbed-Slim-Fit-T-shirt-p-340854-cat-1738.html";
        String url = "http://us.shein.com/US-AW-Trends-vc-34738-p1.html";
        Document doc;
        String title = null;
        String price = null;
        String coverImage = null;
        Integer reviewCount = 0;
        long now = System.currentTimeMillis();
        try {
            doc = Jsoup.connect(url).get();
            System.out.println(doc);

            int dataIndex = doc.html().indexOf("var goodsListData=");
            if (dataIndex != -1) {
                String data = doc.html().substring(dataIndex + 18);
                dataIndex = data.indexOf("}];");
                if (dataIndex != -1) {
                    data = data.substring(0, dataIndex + 2);
                    System.out.println(data);
                }
                String newData = "{\"sheinProducts\":" + data + "}";

                ObjectMapper om = new ObjectMapper();
                om.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                SheinProductList sheinProducts = om.readValue(newData, SheinProductList.class);
                System.out.println(sheinProducts.getSheinProducts().size());
            }



//            try {
//                Thread.currentThread().sleep(2000);
//            } catch (Exception e) {
//            }
//            Elements titleElement = doc.getElementsByAttributeValue("class", "name");
//            if (!titleElement.isEmpty()) {
//                title = titleElement.get(0).text();
//                System.out.println(title);
//            }
//
//            int indexShopPrice = doc.html().indexOf("\"shop_price\"");
//            int indexSpecialPrice = doc.html().indexOf("\"special_price\"");
//            // Shop price
//            String shopPriceStr = doc.html().substring(indexShopPrice + 14);
//            int quotes = shopPriceStr.indexOf("\"");
//            String shopPrice = shopPriceStr.substring(0, quotes);
//            // Sale price
//            String specialPriceStr = doc.html().substring(indexSpecialPrice + 16);
//            int comma = specialPriceStr.indexOf(",");
//            String salePrice = specialPriceStr.substring(0, comma);
//            if ("0".equals(salePrice)) {
//                salePrice = shopPrice;
//            } else {
//                salePrice = salePrice + ".00";
//            }
//            // Cover image
//            Elements imageElements = doc.getElementsByAttributeValue("onload", "imgDetailLoadGa()");
//            if (!imageElements.isEmpty()) {
//                if (imageElements.get(0).hasAttr("src")) {
//                    coverImage = imageElements.get(0).attr("src");
//                    System.out.println(coverImage);
//                }
//            }
//            // Review count
//            Elements reviewsElements = doc.getElementsByAttributeValue("class", "cmtNum");
//            if (!reviewsElements.isEmpty()) {
//                String reviews = reviewsElements.get(0).text();
//                if (!StringUtils.isEmpty(reviews)) {
//                    reviewCount = Integer.parseInt(reviews);
//                }
//                System.out.println(reviewCount);
//            }

        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}
