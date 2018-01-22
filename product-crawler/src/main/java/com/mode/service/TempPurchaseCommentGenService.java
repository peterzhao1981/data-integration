package com.mode.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.mode.entity.ShopifyProduct;
import com.mode.entity.TempPurchase;
import com.mode.repository.ShopifyProductRepository;
import com.mode.repository.TempPurchaseRepository;

/**
 * Created by zhaoweiwei on 2017/12/13.
 */
@Service
public class TempPurchaseCommentGenService {

    @Autowired
    private TempPurchaseRepository tempPurchaseRepository;

    @Autowired
    private ShopifyProductRepository shopifyProductRepository;

    public void process() {
        List<String> tempNoList = tempPurchaseRepository.findDistinctTempNo();
        for (String tempNo : tempNoList) {
            String comment = "";
            String failedReason = null;
            List<TempPurchase> tempPurchaseList = tempPurchaseRepository.findByTempNo(tempNo);
            System.out.println(tempPurchaseList.size());
            for (TempPurchase tempPurchase : tempPurchaseList) {
                String sku = tempPurchase.getSku();
                String quantity = tempPurchase.getQuantity();
                String skuStr = "'" + sku;
                ShopifyProduct shopifyProduct = shopifyProductRepository.findTopByVariantSkuOrderByHandle(skuStr);
                if (shopifyProduct == null) {
                    shopifyProduct = shopifyProductRepository.findTopByVariantSkuOrderByHandle(sku);

                }
                if (shopifyProduct == null) {
                    System.out.println("shopifyProduct is null for sku : " + sku);
                }
                if (shopifyProduct != null) {
                    StringBuffer sb = new StringBuffer();
                    String option1Value = shopifyProduct.getOption1Value();
                    String option2Value = shopifyProduct.getOption2Value();
                    String option3Value = shopifyProduct.getOption3Value();
                    if (!StringUtils.isEmpty(option1Value)) {
                        sb.append(option1Value);
                    }
                    if (!StringUtils.isEmpty(option2Value)) {
                        if (sb.toString().length() > 0) {
                            sb.append(" / ");
                        }
                        sb.append(option2Value);
                    }
                    if (!StringUtils.isEmpty(option3Value)) {
                        if (sb.toString().length() > 0) {
                            sb.append(" / ");
                        }
                        sb.append(option3Value);
                    }
                    if (!StringUtils.isEmpty(sb.toString())) {
                        if ("".equals(comment)) {
                            comment = sku + "-" + sb.toString() + "-" + quantity;
                        } else {
                            comment = comment + "," + sku + "-" + sb.toString() + "-" + quantity;
                        }
                    } else {
                        failedReason = "Option value is empty for sku : " + sku;
                        break;
                    }
                }
            }
            TempPurchase tempPurchase = tempPurchaseList.get(0);
            if (!StringUtils.isEmpty(failedReason)) {
                tempPurchase.setFailedReason(failedReason);
                tempPurchaseRepository.save(tempPurchase);
            } else {
                if (!StringUtils.isEmpty(tempPurchase.getComment())) {
                    comment = tempPurchase.getComment() + "," + comment;
                }
                tempPurchase.setComment(comment);
                tempPurchaseRepository.save(tempPurchase);
            }
        }
    }
}
