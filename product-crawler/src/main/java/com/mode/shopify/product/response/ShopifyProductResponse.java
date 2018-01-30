package com.mode.shopify.product.response;

import com.mode.shopify.product.model.ShopifyProduct;

public class ShopifyProductResponse {

    ShopifyProduct product;

    public ShopifyProduct getProduct() {
        return product;
    }

    public void setProduct(ShopifyProduct product) {
        this.product = product;
    }

    @Override
    public String toString() {
        return "ShopifyProductResponse{" +
                "product=" + product +
                '}';
    }
}
