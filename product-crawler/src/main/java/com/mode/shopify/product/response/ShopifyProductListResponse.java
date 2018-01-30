package com.mode.shopify.product.response;

import java.util.List;

import com.mode.shopify.product.model.ShopifyProduct;

public class ShopifyProductListResponse {

    protected List<ShopifyProduct> products;

    public List<ShopifyProduct> getProducts() {
        return products;
    }

    public void setProducts(List<ShopifyProduct> products) {
        this.products = products;
    }
}
