package com.mode.entity;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/*
 * @author maxiaodong on 2018/05/10
 * @version 0.0.1
 * @describe 爬取商品价格
 */
@Entity
@Table(name = "product_price_store", indexes = {})
public class ProductPriceStore {
    @Id
    @Column(columnDefinition = "bigint(13)")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(length = 100)
    private String spu;

    @Column(length = 1000)
    private String productUrl;

    @Column(length = 100)
    private BigDecimal price;

    public Long getId() {
        return id;
    }

    public String getSpu() {
        return spu;
    }

    public void setSpu(String spu) {
        this.spu = spu;
    }

    public String getProductUrl() {
        return productUrl;
    }

    public void setProductUrl(String productUrl) {
        this.productUrl = productUrl;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

}
