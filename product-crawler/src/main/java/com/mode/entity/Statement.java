package com.mode.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.jsoup.helper.StringUtil;
import org.springframework.util.StringUtils;

/**
 * Created by zhaoweiwei on 2017/11/22.
 */
@Entity
@Table(name = "md_statement",
        indexes = {
        }
)
public class Statement {

    @Id
    @Column(columnDefinition = "bigint(13)")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(length = 50)
    private String orderNo;

    @Column(length = 50)
    private String orderStatus;

    @Column(length = 1000)
    private String sku;

    @Column(length = 500)
    private String skuDetail;

    @Column(length = 30)
    private String platform;

    @Column(columnDefinition = "text")
    private String productName;

    @Column(length = 10)
    private String currency;

    @Column(length = 10)
    private String totalCost;

    @Column(length = 30)
    private String logisticsTrackNo;

    @Column(length = 30)
    private String paidAt;

    @Column(length = 30)
    private String orderedAt;

    @Column(length = 200)
    private String skuFactoryPrice;

    @Column(length = 10)
    private String factoryPrice;

    @Column(length = 10)
    private String logisticsPrice;

    @Column(length = 20)
    private String calStatus;

    @Column(length = 200)
    private String logisticsWarning;

    @Column(length = 200)
    private String factoryPriceWarning;

    @Column(length = 10)
    private String factoryPricePercentage;

    @Column(length = 10)
    private String logisticsPricePercentage;

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

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
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

    public String getFactoryPrice() {
        return factoryPrice;
    }

    public void setFactoryPrice(String factoryPrice) {
        this.factoryPrice = factoryPrice;
    }

    public String getLogisticsPrice() {
        return logisticsPrice;
    }

    public void setLogisticsPrice(String logisticsPrice) {
        this.logisticsPrice = logisticsPrice;
    }

    public String getCalStatus() {
        return calStatus;
    }

    public void setCalStatus(String calStatus) {
        this.calStatus = calStatus;
    }

    public String getLogisticsWarning() {
        return logisticsWarning;
    }

    public void setLogisticsWarning(String logisticsWarning) {
        this.logisticsWarning = logisticsWarning;
    }

    public String getFactoryPriceWarning() {
        return factoryPriceWarning;
    }

    public void setFactoryPriceWarning(String factoryPriceWarning) {
        this.factoryPriceWarning = factoryPriceWarning;
    }

    public String getSkuFactoryPrice() {
        return skuFactoryPrice;
    }

    public void setSkuFactoryPrice(String skuFactoryPrice) {
        this.skuFactoryPrice = skuFactoryPrice;
    }

    public String getSkuDetail() {
        return skuDetail;
    }

    public void setSkuDetail(String skuDetail) {
        this.skuDetail = skuDetail;
    }

    public Statement appendFactoryPriceWarning(String factoryPriceWarning) {
        if (!StringUtils.isEmpty(this.getFactoryPrice())) {
            this.setFactoryPriceWarning(this.getFactoryPrice() + " && " + factoryPriceWarning);
        } else {
            this.setFactoryPriceWarning(factoryPriceWarning);
        }
        return this;
    }

    public String getFactoryPricePercentage() {
        return factoryPricePercentage;
    }

    public void setFactoryPricePercentage(String factoryPricePercentage) {
        this.factoryPricePercentage = factoryPricePercentage;
    }

    public String getLogisticsPricePercentage() {
        return logisticsPricePercentage;
    }

    public void setLogisticsPricePercentage(String logisticsPricePercentage) {
        this.logisticsPricePercentage = logisticsPricePercentage;
    }
}
