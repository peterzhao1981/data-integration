package com.mode.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * Created by zhaoweiwei on 2017/11/22.
 */
@Entity
@Table(name = "md_order",
        indexes = {
        }
)
@JsonFormat
public class Order {

    @Id
    @Column(columnDefinition = "bigint(13)")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(length = 50)
    private String orderNo;

    @Column(length = 50)
    private String orderStatus;

    @Column(name = "SKU", length = 1000)
    private String sku;

    @Column(length = 30)
    private String platform;

    @Column(columnDefinition = "text")
    private String skuName;

    @Column(columnDefinition = "text")
    private String productName;

    @Column(length = 250)
    private String customerServiceComment;

    @Column(length = 10)
    private String currency;

    @Column(length = 500)
    private String skuDetail;

    @Column(length = 10)
    private String totalCost;

    @Column(length = 30)
    private String logisticsTrackNo;

    @Column(length = 200)
    private String parcelNo;

    @Column(length = 200)
    private String skuFactoryPrice;

    @Column(length = 30)
    private String paidAt;

    @Column(name = "ordered_at", length = 30)
    private String orderedAt;



    @Column(length = 20)
    private String status;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }



    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(String totalCost) {
        this.totalCost = totalCost;
    }

    public String getLogisticsTrackNo() {
        return logisticsTrackNo;
    }

    public void setLogisticsTrackNo(String logisticsTrackNo) {
        this.logisticsTrackNo = logisticsTrackNo;
    }

    public String getPaidAt() {
        return paidAt;
    }

    public void setPaidAt(String paidAt) {
        this.paidAt = paidAt;
    }

    public String getOrderedAt() {
        return orderedAt;
    }

    public void setOrderedAt(String orderedAt) {
        this.orderedAt = orderedAt;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCustomerServiceComment() {
        return customerServiceComment;
    }

    public void setCustomerServiceComment(String customerServiceComment) {
        this.customerServiceComment = customerServiceComment;
    }

    public String getSkuDetail() {
        return skuDetail;
    }

    public void setSkuDetail(String skuDetail) {
        this.skuDetail = skuDetail;
    }

    public String getParcelNo() {
        return parcelNo;
    }

    public void setParcelNo(String parcelNo) {
        this.parcelNo = parcelNo;
    }

    public String getSkuFactoryPrice() {
        return skuFactoryPrice;
    }

    public void setSkuFactoryPrice(String skuFactoryPrice) {
        this.skuFactoryPrice = skuFactoryPrice;
    }

    public String getSkuName() {
        return skuName;
    }

    public void setSkuName(String skuName) {
        this.skuName = skuName;
    }
}
