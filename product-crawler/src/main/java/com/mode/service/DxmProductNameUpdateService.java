package com.mode.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.mode.entity.DxmProduct;
import com.mode.repository.DxmProductRepository;
import com.mode.util.Bao69HttpUtils;
import com.mode.util.Detail1688HttpUtils;
import com.mode.util.HaoDuoYiHttpUtils;
import com.mode.util.TaoBaoHttpUtils;
import com.mode.util.TmallHttpUtils;

/**
 * Created by zhaoweiwei on 2018/1/15.
 */

/**
 * Convert dxm product name from en to cn.
 */
@Service
public class DxmProductNameUpdateService {

    @Autowired
    private DxmProductRepository dxmProductRepository;

    private Map<String, String> map = new HashMap<>();

    public void process() {
//        List<DxmProduct> dxmProducts = dxmProductRepository.findAll();
        List<DxmProduct> dxmProducts = dxmProductRepository.findByProductNameCnIsNull();
        for (DxmProduct dxmProduct : dxmProducts) {
            String sourceUrl = dxmProduct.getSourceUrl();
            String productNameCn;
            if (!StringUtils.isEmpty(sourceUrl)) {
                sourceUrl = sourceUrl.replaceAll(" ", "");
                if (map.containsKey(sourceUrl)) {
                    productNameCn = map.get(sourceUrl);
                } else {
                    if (sourceUrl.indexOf("item.taobao.com") != -1) {
                        productNameCn = TaoBaoHttpUtils.getProductName(sourceUrl);
                    } else if (sourceUrl.indexOf("detail.tmall.com") != -1) {
                        productNameCn = TmallHttpUtils.getProductName(sourceUrl);
                    } else if (sourceUrl.indexOf("haoduoyi.com") != -1) {
                        productNameCn = HaoDuoYiHttpUtils.getProductName(sourceUrl);
                    } else if (sourceUrl.indexOf("1688.com") != -1) {
                        productNameCn = Detail1688HttpUtils.getProductName(sourceUrl);
                    } else if (sourceUrl.indexOf("bao69.com") != -1) {
                        productNameCn = Bao69HttpUtils.getProductName(sourceUrl);
                    } else {
                        System.out.println(dxmProduct.getSku() + " " + dxmProduct.getSourceUrl() + " can't process");
                        map.put(sourceUrl, null);
                        continue;
                    }
                    map.put(sourceUrl, productNameCn);
                }
                if (productNameCn != null) {
                    dxmProduct.setProductNameCn(productNameCn);
                    dxmProductRepository.save(dxmProduct);
                }
            }
        }
    }
}
