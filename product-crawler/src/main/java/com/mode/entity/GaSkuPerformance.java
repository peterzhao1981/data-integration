package com.mode.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by zhaoweiwei on 2017/11/28.
 */
@Entity
@Table(name = "ga_sku_performance",
        indexes = {
        }
)
public class GaSkuPerformance {

    @Id
    @Column(columnDefinition = "bigint(13)")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(length = 250)
    private String skuName;

    @Column(length = 250)
    private String spuName;

    @Column(length = 250)
    private String brand;

    @Column(length = 20)
    private String detailViews;

    @Column(length = 20)
    private String addToCart;

    @Column(length = 20)
    private String revenue;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSkuName() {
        return skuName;
    }

    public void setSkuName(String skuName) {
        this.skuName = skuName;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getDetailViews() {
        return detailViews;
    }

    public void setDetailViews(String detailViews) {
        this.detailViews = detailViews;
    }

    public String getAddToCart() {
        return addToCart;
    }

    public void setAddToCart(String addToCart) {
        this.addToCart = addToCart;
    }

    public String getRevenue() {
        return revenue;
    }

    public void setRevenue(String revenue) {
        this.revenue = revenue;
    }

    public String getSpuName() {
        return spuName;
    }

    public void setSpuName(String spuName) {
        this.spuName = spuName;
    }
}
