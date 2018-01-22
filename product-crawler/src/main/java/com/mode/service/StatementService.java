package com.mode.service;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.mode.entity.DxmProduct;
import com.mode.entity.Order;
import com.mode.entity.Parcel;
import com.mode.entity.Statement;
import com.mode.repository.DxmProductRepository;
import com.mode.repository.OrderRepository;
import com.mode.repository.ParcelRepository;
import com.mode.repository.StatementRepository;

/**
 * Created by zhaoweiwei on 2017/11/22.
 */
@Service
public class StatementService {

    @Autowired
    private StatementRepository statementRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ParcelRepository parcelRepository;

    @Autowired
    private DxmProductRepository dxmProductRepository;

    private float rateUsdToRmb = 6.608f;

    public static DecimalFormat df = new java.text.DecimalFormat("#.##");

    public void process() {
        List<Order> orders = orderRepository.findAll();
        for (Order order : orders) {
            String orderNo = order.getOrderNo();
            String orderStatus = order.getOrderStatus();
            String sku = order.getSku();
            String platform = order.getPlatform();
            String productName = order.getSkuName();
            String currency = order.getCurrency();
            String totalCost = order.getTotalCost();
            String logisticsTrackNo = order.getLogisticsTrackNo();
            String skuDetail = order.getSkuDetail();
            String skuFactoryPrice = order.getSkuFactoryPrice();
            String parcelNo = order.getParcelNo();
            String paidAt = order.getPaidAt();
            String orderedAt = order.getOrderedAt();
            Statement statement = new Statement();
            statement.setOrderNo(orderNo);
            statement.setOrderStatus(orderStatus);
            statement.setSku(sku);
            statement.setPlatform(platform);
            statement.setProductName(productName);
            statement.setCurrency(currency);
            statement.setTotalCost(totalCost);
            statement.setLogisticsTrackNo(logisticsTrackNo);
            statement.setSkuDetail(skuDetail);
            statement.setSkuFactoryPrice(skuFactoryPrice);
            statement.setPaidAt(paidAt);
            statement.setOrderedAt(orderedAt);

            Parcel parcel = parcelRepository.findOneByParcelNo(parcelNo);
            if (parcel != null) {
                statement.setLogisticsPrice(df.format(Float.parseFloat(parcel.getTotalFee()) / rateUsdToRmb));
            } else {
                parcel = parcelRepository.findOneByParcelNo(orderNo);
                String fee = null;
                if (parcel != null) {
                    fee = parcel.getTotalFee();

                }
                if (!StringUtils.isEmpty(order.getCustomerServiceComment())) {
                    orderNo = orderNo + "-1";
                    parcel = parcelRepository.findOneByParcelNo(orderNo);
                    if (parcel != null) {
                        if (!StringUtils.isEmpty(fee)) {
                            fee = String.valueOf(Float.parseFloat(fee) + Float.parseFloat(parcel.getTotalFee()));
                        }

                    }
                }
                if (!StringUtils.isEmpty(fee)) {
                    statement.setLogisticsPrice(df.format(Float.parseFloat(fee) / rateUsdToRmb));
                }

            }
            statement = setFactoryPrice(skuDetail, skuFactoryPrice, statement);
            statement = setPricePercentage(statement);

            statementRepository.save(statement);
        }
    }

    private Statement setFactoryPrice(String skuDetail, String skuFactoryPrice, Statement statement) {

        if (StringUtils.isEmpty(skuDetail)) {
            statement.appendFactoryPriceWarning("没有sku数量信息");
            return statement;
        }
        String[] skuDetailList = skuDetail.split("\r");
        List<Sku> skus = new ArrayList<>();
        boolean getFromDxmProduct = false;
        if (StringUtils.isEmpty(skuFactoryPrice)) {
            getFromDxmProduct = true;
        } else {
            String[] skuFactoryPriceList = skuFactoryPrice.split("\r");
            if (skuDetailList.length != skuFactoryPriceList.length) {
                getFromDxmProduct = true;
            }
        }

        if (getFromDxmProduct) {
//            statement.appendFactoryPriceWarning("没有sku价格信息");
            for (int i = 0 ; i < skuDetailList.length; i ++) {
                String detail = skuDetailList[i];
                String[] str = detail.split("\\*");
                if (!StringUtils.isEmpty(str[0])) {
                    DxmProduct dxmProduct = dxmProductRepository.findOneBySku(str[0]);
                    if (dxmProduct != null) {
                        Sku sku = new Sku();
                        sku.setSku(str[0]);
                        sku.setQuantity(Integer.parseInt(str[1]));
                        if (Float.parseFloat(dxmProduct.getFactoryPrice()) == 0l) {
                            statement.appendFactoryPriceWarning("Sku : " + str[0] + "采购价为0");
                        }
                        sku.setPrice(Float.parseFloat(dxmProduct.getFactoryPrice()));
                        skus.add(sku);
                    } else {
                        statement.appendFactoryPriceWarning("店小秘找不到商品sku:" + str[0]);
                        return statement;
                    }
                } else {
                    statement.appendFactoryPriceWarning("Sku数量信息有误");
                    return statement;
                }
            }
        } else {
            String[] skuFactoryPriceList = skuFactoryPrice.split("\r");

            if (skuDetailList.length == skuFactoryPriceList.length) {
                for (int i = 0 ; i < skuDetailList.length; i ++) {
                    String detail = skuDetailList[i];
                    String price = skuFactoryPriceList[i];
                    if (!StringUtils.isEmpty(detail) && !StringUtils.isEmpty(price)) {
                        String[] str = detail.split("\\*");
                        Sku sku = new Sku();
                        sku.setSku(str[0]);
                        sku.setQuantity(Integer.parseInt(str[1]));
                        if (Float.parseFloat(price) == 0l) {
                            statement.appendFactoryPriceWarning("Sku : " + str[0] + "采购价为0");
                        }
                        sku.setPrice(Float.parseFloat(price));
                        skus.add(sku);
                    }
                }
            }
//            else {
//                statement.appendFactoryPriceWarning("Sku数量和sku价格中的sku不匹配");
//                return statement;
//            }
        }


        float factoryPrice = 0l;
        if (skus.size() == 0) {
            return statement;
        }
        for (Sku sku : skus) {
            factoryPrice = factoryPrice + sku.getPrice() * sku.getQuantity();
        }
        statement.setFactoryPrice(df.format(factoryPrice / rateUsdToRmb));
        return statement;
    }

    public void updatePercentage() {
        List<Statement> statements = statementRepository.findAll();
        for (Statement statement : statements) {
            statement = setPricePercentage(statement);
            statementRepository.save(statement);
        }
    }

    private Statement setPricePercentage(Statement statement) {
        String totalCost = statement.getTotalCost();
        String factoryPrice = statement.getFactoryPrice();
        String logisticsPrice = statement.getLogisticsPrice();

        if (!StringUtils.isEmpty(totalCost)) {
            float totalCostFloat = Float.parseFloat(totalCost);
            if (Float.parseFloat(totalCost) > 0) {
                if (!StringUtils.isEmpty(factoryPrice)) {
                    float factoryPriceFloat = Float.parseFloat(factoryPrice);
                    statement.setFactoryPricePercentage(df.format((factoryPriceFloat  / totalCostFloat) * 100));
                }
                if (!StringUtils.isEmpty(logisticsPrice)) {
                    float logisticsPriceFloat = Float.parseFloat(logisticsPrice);
                    statement.setLogisticsPricePercentage(df.format((logisticsPriceFloat  / totalCostFloat) *
                            100));
                }
            }
        }
        return statement;
    }


    private class Sku {
        private String sku;
        private Integer quantity;
        private Float price;

        public String getSku() {
            return sku;
        }

        public void setSku(String sku) {
            this.sku = sku;
        }

        public Integer getQuantity() {
            return quantity;
        }

        public void setQuantity(Integer quantity) {
            this.quantity = quantity;
        }

        public Float getPrice() {
            return price;
        }

        public void setPrice(Float price) {
            this.price = price;
        }
    }

    public static void main(String[] args) {
        StatementService s = new StatementService();
        float a = 15.9f;
        float b = 4.3f;
        System.out.println(df.format((b / a) * 100));
    }
}
