package com.mode.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.mode.entity.DxmInventory;
import com.mode.entity.DxmSkuMapping;
import com.mode.entity.ShopifyProduct;
import com.mode.repository.DxmInventoryRepository;
import com.mode.repository.DxmSkuMappingRespository;
import com.mode.repository.ShopifyProductRepository;

/**
 * Created by zhaoweiwei on 2017/12/12.
 */
@Service
public class NewSkuGenService {

    @Autowired
    private DxmInventoryRepository dxmInventoryRepository;

    @Autowired
    private ShopifyProductRepository shopifyProductRepository;

    @Autowired
    private DxmSkuMappingRespository dxmSkuMappingRespository;

    public void process() {
        List<DxmInventory> dxmInventoryList = dxmInventoryRepository.findAll();
        int start = 10000;
        Map<String, Integer> productMap = new HashMap<>();

        for (DxmInventory dxmInventory : dxmInventoryList) {
            String sku = dxmInventory.getSku();
//            Integer skuInt = null;
            if (!StringUtils.isEmpty(sku)) {
//                try {
//                    skuInt = Integer.parseInt(sku);
//                } catch (Exception e) {
//                    continue;
//                }
                //if (skuInt != null) {
                    String skuStr = "'" + sku;
                    ShopifyProduct shopifyProduct = shopifyProductRepository.findTopByVariantSkuOrderByHandle(skuStr);
                    if (shopifyProduct == null) {
                        shopifyProduct = shopifyProductRepository.findTopByVariantSkuOrderByHandle(sku);
                    }
                    if (shopifyProduct != null) {
                        String handle = shopifyProduct.getHandle();
                        Integer spu = productMap.get(handle);
                        if (spu == null) {
                            spu = ++start;
                            productMap.put(handle, spu);
                        }

                        StringBuffer sb = new StringBuffer();
                        String option1Value = shopifyProduct.getOption1Value();
                        String option2Value = shopifyProduct.getOption2Value();
                        String option3Value = shopifyProduct.getOption3Value();
                        if (!StringUtils.isEmpty(option1Value)) {
                            sb.append(option1Value);
                        }
                        if (!StringUtils.isEmpty(option2Value)) {
                            if (sb.toString().length() > 0) {
                                sb.append("-");
                            }
                            sb.append(option2Value);
                        }
                        if (!StringUtils.isEmpty(option3Value)) {
                            if (sb.toString().length() > 0) {
                                sb.append("-");
                            }
                            sb.append(option3Value);
                        }
                        if (!StringUtils.isEmpty(sb.toString())) {
                            String newSku = "p" + spu + "-" + sku + "-" + sb.toString();
                            dxmInventory.setNewSku(newSku.replaceAll(" ", ""));
                            dxmInventory.setWarehousePosition("上海自建仓");
                            dxmInventoryRepository.save(dxmInventory);
                        }

                    }
                //}
            }
        }
    }

    public void process1() {
        List<DxmSkuMapping> dxmSkuMappingList = dxmSkuMappingRespository.findAll();
        int start = 10000;
        Map<String, Integer> productMap = new HashMap<>();

        for (DxmSkuMapping dxmSkuMapping : dxmSkuMappingList) {
            String sku = dxmSkuMapping.getSku();
//            Integer skuInt = null;
            if (!StringUtils.isEmpty(sku)) {
//                try {
//                    skuInt = Integer.parseInt(sku);
//                } catch (Exception e) {
//                    continue;
//                }
//                if (skuInt != null) {
                    String skuStr = "'" + sku;
                    ShopifyProduct shopifyProduct = shopifyProductRepository.findTopByVariantSkuOrderByHandle(skuStr);
                    if (shopifyProduct == null) {
                        shopifyProduct = shopifyProductRepository.findTopByVariantSkuOrderByHandle(sku);
                    }
                    if (shopifyProduct != null) {
                        String handle = shopifyProduct.getHandle();
                        Integer spu = productMap.get(handle);
                        if (spu == null) {
                            spu = ++start;
                            productMap.put(handle, spu);
                        }

                        StringBuffer sb = new StringBuffer();
                        String option1Value = shopifyProduct.getOption1Value();
                        String option2Value = shopifyProduct.getOption2Value();
                        String option3Value = shopifyProduct.getOption3Value();
                        if (!StringUtils.isEmpty(option1Value)) {
                            sb.append(option1Value);
                        }
                        if (!StringUtils.isEmpty(option2Value)) {
                            if (sb.toString().length() > 0) {
                                sb.append("-");
                            }
                            sb.append(option2Value);
                        }
                        if (!StringUtils.isEmpty(option3Value)) {
                            if (sb.toString().length() > 0) {
                                sb.append("-");
                            }
                            sb.append(option3Value);
                        }
                        if (!StringUtils.isEmpty(sb.toString())) {
                            String newSku = spu + "-" + sku + "-" + sb.toString();
                            dxmSkuMapping.setNewSku(newSku.replaceAll(" ", ""));
//                            dxmInventory.setWarehousePosition("上海自建仓");
                            dxmSkuMappingRespository.save(dxmSkuMapping);
                        }

                    }
//                }
            }
        }
    }

    public static void main(String[] args) {
        int start = 10000;
        int spu =  ++ start;
        System.out.println(start);
        System.out.println(spu);
        StringBuffer sb = new StringBuffer();
        System.out.println(sb.toString());
    }
}
