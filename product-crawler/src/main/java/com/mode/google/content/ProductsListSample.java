package com.mode.google.content;

/**
 * Created by zhaoweiwei on 2018/1/2.
 */

import java.io.IOException;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.springframework.util.StringUtils;

import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.services.content.ShoppingContent;
import com.google.api.services.content.model.Product;
import com.google.api.services.content.model.ProductsListResponse;


/**
 * Sample that gets a list of all of the products for the merchant. If there is more than one page
 * of results, we fetch each page in turn.
 */
public class ProductsListSample extends ContentSample {

    private static Map<String, String> map = new HashMap<>();
    private static Set<String> set = new HashSet<>();

    static {
        map.put("Beauty", "Health & Beauty"); // 469
        map.put("Tops", "Apparel & Accessories > Clothing > Shirts & Tops"); // 212
        map.put("Dresses", "Apparel & Accessories > Clothing > Dresses"); // 2271
        map.put("Sweaters & Cardigans", "Apparel & Accessories > Clothing > Outerwear > Coats & Jackets"); // 5598
        map.put("Swimwear & Beachwear", "Apparel & Accessories > Clothing > Swimwear"); // 211
        map.put("Boots", "Apparel & Accessories > Shoes"); // 187
        map.put("Jewelry", "Apparel & Accessories > Jewelry"); // 188
        map.put("Pants & Shorts", "Apparel & Accessories > Clothing > Pants"); // 204
        map.put("Pants&Shorts", "Apparel & Accessories > Clothing > Pants"); // 204
        map.put("Coats & Jackets", "Apparel & Accessories > Clothing > Outerwear > Coats & Jackets"); // 5598
        map.put("Shoes", "Apparel & Accessories > Shoes"); // 187
        map.put("Skirts", "Apparel & Accessories > Clothing > Skirts"); // 1581
        map.put("Hoodies", "Apparel & Accessories > Clothing > Outerwear > Coats & Jackets"); // 5598
        map.put("Pumps", "Apparel & Accessories > Shoes"); // 187
        map.put("Lingerie & Sleepwear", "Apparel & Accessories > Clothing > Underwear & Socks > Lingerie"); // 1772
        map.put("Hoodies & Sweatshirts", "Apparel & Accessories > Clothing > Outerwear > Coats & Jackets"); // 5598
        map.put("Jumpsuits & Playsuits", "Apparel & Accessories > Clothing > One-Pieces > Jumpsuits & Rompers"); // 5250
        map.put("Sunglasses", "Apparel & Accessories > Clothing Accessories > Sunglasses"); // 178
        map.put("Socks & Tights", "Apparel & Accessories > Clothing > Underwear & Socks > Socks"); // 209
        map.put("Hats", "Apparel & Accessories > Clothing Accessories > Hats"); // 173
        map.put("Cross Body Bags", "Apparel & Accessories > Handbags, Wallets & Cases"); // 6551
        map.put("Shoulder Bags", "Apparel & Accessories > Handbags, Wallets & Cases"); // 6551
        map.put("Stationery & Organization", "Office Supplies > General Office Supplies > Paper Products > Stationery"); // 3457
        map.put("Dress", "Apparel & Accessories > Clothing > Dresses"); // 2271
        map.put("Bags", "Apparel & Accessories > Handbags, Wallets & Cases"); // 6551
        map.put("Apparel", "Apparel & Accessories > Clothing"); // 1604
        map.put("Scarves", "Apparel & Accessories > Clothing Accessories > Scarves & Shawls"); // 177
        map.put("Accessories", "Apparel & Accessories > Clothing Accessories"); // 167
        map.put("Clutches & Purses", "Apparel & Accessories > Handbags, Wallets & Cases > Handbags"); // 3032
        map.put("Activewear", "Apparel & Accessories > Clothing > Activewear"); // 5322
        map.put("Tote Bags", "Apparel & Accessories > Handbags, Wallets & Cases > Handbags"); // 3032
        map.put("Backpacks", "Apparel & Accessories > Handbags, Wallets & Cases"); // 6551
        map.put("Flats", "Apparel & Accessories > Shoes"); // 187
        map.put("Sneakers", "Apparel & Accessories > Shoes"); // 187
        map.put("Hair Accessories", "Apparel & Accessories > Clothing Accessories > Hair Accessories"); // 171
        map.put("Living Room", "Furniture > Furniture Sets > Living Room Furniture Sets"); // 6348
    }

    public ProductsListSample(String[] args) throws IOException {
        super(args);
    }

//    static void listProductsForMerchant(BigInteger merchantId, ShoppingContent content)
//            throws IOException {
//        ShoppingContent.Products.List productsList = content.products().list(merchantId);
//        int i = 0;
//        File file = new ClassPathResource("feed.txt").getFile();
//        FileWriter fileWriter = new FileWriter(file, true);
//        BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
//        try {
//            do {
//                ProductsListResponse page = productsList.execute();
//                if (page.getResources() == null) {
//                    System.out.println("No products found.");
//                    return;
//                }
//                for (Product product : page.getResources()) {
//                    String offerId = product.getOfferId();
//                    String productType = product.getProductType();
//                    String googleProductCategory = product.getGoogleProductCategory();
//                    if (StringUtils.isEmpty(googleProductCategory)) {
//                        i ++;
//                        if (!StringUtils.isEmpty(productType)) {
//                            googleProductCategory = map.get(productType);
//                            if (!StringUtils.isEmpty(googleProductCategory)) {
//                                StringBuffer sb = new StringBuffer();
//                                String ageGroup = StringUtils.isEmpty(product.getAgeGroup()) ? "adult" : product
//                                        .getAgeGroup();
//                                String gender = StringUtils.isEmpty(product.getGender()) ? "female" : product
//                                        .getGender();
//                                sb.append(offerId);
//                                sb.append("\t");
//                                sb.append(googleProductCategory);
//                                sb.append("\t");
//                                sb.append(ageGroup);
//                                sb.append("\t");
//                                sb.append(gender);
//                                System.out.println(sb.toString());
//                                bufferedWriter.write(sb.toString());
//                                bufferedWriter.newLine();
//                            }
//                        }
//                    }
//
//                }
//                if (page.getNextPageToken() == null) {
//                    break;
//                }
//                productsList.setPageToken(page.getNextPageToken());
//            } while (true);
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            if (bufferedWriter != null) {
//                bufferedWriter.close();
//            }
//            if (fileWriter != null) {
//                fileWriter.close();
//            }
//        }
//        System.out.println(i);
//    }

//    static void testUpdate(BigInteger merchantId, ShoppingContent content)
//            throws IOException {
//        ShoppingContent.Products.List productsList = content.products().list(merchantId);
//        boolean ctn = true;
//
//        do {
//            ProductsListResponse page = productsList.execute();
//            if (page.getResources() == null) {
//                System.out.println("No products found.");
//                return;
//            }
//            for (Product product : page.getResources()) {
//                String offerId = product.getOfferId();
//                if ("shopify_US_289499775003_3937601388571".equals(offerId)) {
//                    System.out.println(product);
//                    ctn = false;
//                    break;
//                }
//
//            }
//            if (ctn == false) {
//                break;
//            }
//            if (page.getNextPageToken() == null) {
//                break;
//            }
//            productsList.setPageToken(page.getNextPageToken());
//        } while (true);
//
//
//    }
public void listProductsForMerchant(BigInteger merchantId, ShoppingContent content)
        throws IOException {
    ShoppingContent.Products.List productsList = content.products().list(merchantId);
    int i = 0;

    do {
        ProductsListResponse page = productsList.execute();
        if (page.getResources() == null) {
            System.out.println("No products found.");
            return;
        }
        for (Product product : page.getResources()) {
            String offerId = product.getOfferId();
            String productType = product.getProductType();
            String googleProductCategory = product.getGoogleProductCategory();
            if (StringUtils.isEmpty(googleProductCategory)) {
                i++;
                System.out.println(offerId);
                if (!StringUtils.isEmpty(productType)) {
                    googleProductCategory = map.get(productType);
                    if (!StringUtils.isEmpty(googleProductCategory)) {
                        product.setGoogleProductCategory(googleProductCategory);
                        String ageGroup = StringUtils.isEmpty(product.getAgeGroup()) ? "adult" : product
                                        .getAgeGroup();
                        String gender = StringUtils.isEmpty(product.getGender()) ? "female" : product
                                        .getGender();
                        product.setAgeGroup(ageGroup);
                        product.setGender(gender);
                        update(product);
                    }
                }
            }

        }
        if (page.getNextPageToken() == null) {
            break;
        }
        productsList.setPageToken(page.getNextPageToken());
    } while (true);

    System.out.println(i);
}

    static void countGoogleProductCategory(BigInteger merchantId, ShoppingContent content)
            throws IOException {
        ShoppingContent.Products.List productsList = content.products().list(merchantId);
        int i = 0;
        int j = 0;
        int l2 = 0;
        int l3more = 0;
        int l3less = 0;
        do {
            ProductsListResponse page = productsList.execute();
            if (page.getResources() == null) {
                System.out.println("No products found.");
                return;
            }
            for (Product product : page.getResources()) {
                i ++;
                String offerId = product.getOfferId();
                String productType = product.getProductType();
                String googleProductCategory = product.getGoogleProductCategory();
                if (!StringUtils.isEmpty(googleProductCategory)) {
                    Integer count = StringUtils.countOccurrencesOf(googleProductCategory, ">");
                    if (count == 1) {
                        l2 ++;
                    } else if (count >= 2) {
                        l3more ++;
                    }

                    if (count < 2) {
                        l3less ++;
                        String title = product.getTitle();
                        if (StringUtils.isEmpty(title)) {
                            System.out.println("title is null");
                        }
                        set.add(product.getTitle());
                    }
                } else {
                    j ++;
                    System.out.println(product.getTitle());
                }

            }
            if (page.getNextPageToken() == null) {
                break;
            }
            productsList.setPageToken(page.getNextPageToken());
        } while (true);
        System.out.println(i);
        System.out.println(j);
        System.out.println(l2);
        System.out.println(l3more);
        System.out.println(l3less);
        System.out.println(set.size());
        for (String title : set) {
            System.out.println(title);
        }
    }

    @Override
    public void execute() throws IOException {
        checkNonMCA();

        try {
//            testUpdate(config.getMerchantId(), content);
            listProductsForMerchant(config.getMerchantId(), content);
        } catch (GoogleJsonResponseException e) {
            checkGoogleJsonResponseException(e);
        }
    }

    public void update(Product product) throws IOException {
        checkNonMCA();
        try {
            Product result = content.products().insert(config.getMerchantId(), product).execute();
            ContentUtils.printWarnings(result.getWarnings());
        } catch (GoogleJsonResponseException e) {
            checkGoogleJsonResponseException(e);
        }
    }

    public static void main(String[] args) throws IOException {
        new ProductsListSample(args).execute();
    }
}
