package com.mode.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;

/*
 * @author maxiaodong on 2018/05/07
 * @version 0.0.1
 */

@Entity
@Table(name = "check_product_status", indexes = { @Index(columnList = "id") })
public class CheckProductStatus {
    @Id
    @Column(columnDefinition = "bigint(13)")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(length = 100)
    private String spu;

    @Column(length = 100)
    private String sku;

    @Column(length = 1000)
    private String productUrl;

    @Column(length = 200)
    private String status;

    @Column(length = 1000)
    private String lackInfo;

    // 产品编号，有可能需要，非自增
    @Column(columnDefinition = "bigint(13)")
    private Long productId;

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getLackInfo() {
        return lackInfo;
    }

    public void setLackInfo(String lackInfo) {
        this.lackInfo = lackInfo;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

}
