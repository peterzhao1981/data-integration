package com.mode.shopify;


import com.mode.shopify.product.response.ShopifyProductListResponse;
import com.mode.shopify.product.template.ShopifyProductTemplate;
import com.mode.shopify.common.ShopifyConstants;
import com.mode.shopify.config.Configuration;
import com.mode.shopify.util.Http;

/**
 * This is the primary interface to the Shopify Admin API. It is used to
 * interact with:
 * <ul>
 * <li> {@link ShopifyProductTemplate Orders}
 * <li> { ProductApi Products}
 * </ul>
 * <p>
 * Shopify API call limit:
 * <ul>
 * <li> The bucket size is 40 calls (which cannot be exceeded at any given time), with a "leak rate" of 2 calls per
 * second that continually empties the bucket.
 * <li> In general, you can only get a maximum of 250 objects with one API call.
 * <li> You can only update one orderTemplate or product with one API call.
 * </ul>
 * <p>
 * See https://help.shopify.com/api/getting-started/api-call-limit
 */
public class Shopify {

    private final Http http;

    private Configuration configuration;

    private Shopify(String store, String key, String password) {
        configuration = new Configuration(store, key, password);
        http = new Http(configuration);
    }

    public static Shopify getInstance(boolean production) {
        if (production) {
            return new Shopify(
                    ShopifyConstants.SHOPIFY_API_STORE,
                    ShopifyConstants.SHOPIFY_API_KEY,
                    ShopifyConstants.SHOPIFY_API_PASSWORD);
        } else {
            return new Shopify(
                    ShopifyConstants.SHOPIFY_API_STORE_TEST,
                    ShopifyConstants.SHOPIFY_API_APIKEY_TEST,
                    ShopifyConstants.SHOPIFY_API_PASSWORD_TEST);
        }
    }

    public ShopifyProductTemplate shopifyProductTemplate() {
        return new ShopifyProductTemplate(http);
    }

    public static void main(String[] args) {
        ShopifyProductListResponse response = Shopify.getInstance(true).shopifyProductTemplate()
                .getProductsByPage(1, 100);
        System.out.println(response.getProducts().size());
    }
}
