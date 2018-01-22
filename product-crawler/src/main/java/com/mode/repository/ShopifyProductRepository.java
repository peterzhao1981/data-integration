package com.mode.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mode.entity.ShopifyProduct;

/**
 * Created by zhaoweiwei on 2017/12/12.
 */
public interface ShopifyProductRepository extends JpaRepository<ShopifyProduct, Long> {

    ShopifyProduct findTopByVariantSkuOrderByHandle(String variantSku);
}
