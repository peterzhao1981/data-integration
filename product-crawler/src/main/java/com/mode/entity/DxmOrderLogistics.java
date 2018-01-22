package com.mode.entity;

/**
 * Entity for update Dxm order logistics.
 *
 * Created by zhaoweiwei on 2018/1/17.
 */
public class DxmOrderLogistics {

    private String orderNo = "";
    private String logisticsMethod = "";
    private String trackingNo = "";
    private String shippingType = "1";
    private String trackingUrl = "";

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getLogisticsMethod() {
        return logisticsMethod;
    }

    public void setLogisticsMethod(String logisticsMethod) {
        this.logisticsMethod = logisticsMethod;
    }

    public String getTrackingNo() {
        return trackingNo;
    }

    public void setTrackingNo(String trackingNo) {
        this.trackingNo = trackingNo;
    }

    public String getShippingType() {
        return shippingType;
    }

    public void setShippingType(String shippingType) {
        this.shippingType = shippingType;
    }

    public String getTrackingUrl() {
        return trackingUrl;
    }

    public void setTrackingUrl(String trackingUrl) {
        this.trackingUrl = trackingUrl;
    }
}
