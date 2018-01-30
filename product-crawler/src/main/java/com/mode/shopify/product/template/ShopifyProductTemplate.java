package com.mode.shopify.product.template;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ObjectUtils;

import com.mode.common.exception.BadRequestException;
import com.mode.shopify.common.ShopifyConstants;
import com.mode.shopify.common.ShopifyCount;
import com.mode.shopify.product.model.ShopifyProduct;
import com.mode.shopify.product.response.ShopifyProductListResponse;
import com.mode.shopify.product.response.ShopifyProductResponse;
import com.mode.shopify.util.Http;
import com.mode.util.DateUtils;
import com.mode.util.StringUtils;

public class ShopifyProductTemplate {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final Http http;

    public ShopifyProductTemplate(Http http) {
        this.http = http;
    }

    /**
     * Get products by product ids.
     *
     * @param ids
     * @return
     */
    public ShopifyProductListResponse getProducts(Long[] ids) {

        if (ObjectUtils.isEmpty(ids)) {
            logger.error("invalid parameter - ids is null");
            throw new BadRequestException("invalid parameter - ids is null");
        }

        return findProducts(Collections.singletonMap("ids", StringUtils.join(ids, ",")), false);
    }

    /**
     * Get products since the given product ids.
     * <p>
     * Additionally, you can specify the productType and vendor to filter the results.
     *
     * @param productType optional
     * @param vendor      optional
     * @return
     */
    public ShopifyProductListResponse getProducts(String productType, String vendor) {

        Map<String, String> queryParams = new HashMap<>();
        if (!StringUtils.isBlank(productType)) {
            queryParams.put("product_type", productType);
        }
        if (!StringUtils.isBlank(vendor)) {
            queryParams.put("vendor", vendor);
        }

        return findProducts(queryParams, true);
    }

    /**
     * Get products that are created betweeen createdAtMin and createdAtMax.
     * <p>
     * Additionally, you can specify the productType and vendor to filter the results.
     *
     * @param createdAtMin
     * @param createdAtMax optional
     * @param productType  optional
     * @param vendor       optional
     * @return
     */
    public ShopifyProductListResponse getProducts(Long createdAtMin, Long createdAtMax, String productType, String vendor) {

        if (ObjectUtils.isEmpty(createdAtMin)) {
            logger.error("invalid parameter - createdAtMin is null");
            throw new BadRequestException("invalid parameter - createdAtMin is null");
        }
        Map<String, String> queryParams = new HashMap<>();
        queryParams.put("created_at_min", DateUtils.toIso8601(createdAtMin));
        if (!ObjectUtils.isEmpty(createdAtMax)) {
            queryParams.put("created_at_max", DateUtils.toIso8601(createdAtMax));
        }
        if (!StringUtils.isBlank(productType)) {
            queryParams.put("product_type", productType);
        }
        if (!StringUtils.isBlank(vendor)) {
            queryParams.put("vendor", vendor);
        }

        return findProducts(queryParams, true);
    }

    public ShopifyProductListResponse getProductsByPage(Integer page, Integer limit) {

        Map<String, String> queryParams = new HashMap<>();
        if (page != null) {
            queryParams.put("page", page.toString());
        }
        if (limit != null) {
            queryParams.put("limit", limit.toString());
        }

        return findProducts(queryParams, false);
    }

    /**
     * Retrieve a list of products. Return null if empty results or the number of results exceeded limit - 250.
     *
     * @param queryParams
     * @return
     */
    private ShopifyProductListResponse findProducts(Map<String, String> queryParams, boolean checkResultLimit) {
        if (checkResultLimit) {
            ShopifyCount count = countProducts(queryParams);
            System.out.println(count.getCount());
            if (ObjectUtils.isEmpty(count) || count.getCount() <= 0) {
                logger.warn("Warning - found zero products.");
                return null;
            } else if (count.getCount() > ShopifyConstants.SHOPIFY_API_RATE_LIMIT_MAX_RESULT_OJECTS) {
                logger.warn("Warning - exceeded shopify api call limit of max return objects.");
                return null;
            }

        }
        return http.getForObject("/admin/products.json", ShopifyProductListResponse.class, queryParams);
    }

    /**
     * Retrieve a list of products.
     *
     * @param queryParams
     * @return
     */
    private ShopifyCount countProducts(Map<String, String> queryParams) {
        return http.getForObject("/admin/products/count.json", ShopifyCount.class, queryParams);
    }
}
