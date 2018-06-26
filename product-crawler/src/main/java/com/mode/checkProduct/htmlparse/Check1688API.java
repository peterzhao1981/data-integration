package com.mode.checkProduct.htmlparse;

import com.alibaba.ocean.rawsdk.ApiExecutor;
import com.alibaba.ocean.rawsdk.common.SDKResult;
import com.alibaba.product.param.AlibabaAgentProductSimpleGetParam;
import com.alibaba.product.param.AlibabaAgentProductSimpleGetResult;
import com.alibaba.product.param.AlibabaProductProductInfo;
import com.alibaba.product.param.AlibabaProductProductSKUInfo;
import com.alibaba.product.param.AlibabaProductSKUAttrInfo;
import com.mode.checkProduct.commoninfo.Common;
import com.mode.checkProduct.commoninfo.ConfigInfo;
import com.mode.checkProduct.commoninfo.Switch1688KeyIndex;

/*
 * @author maxiaodong on 2018/05/17
 * @version 0.0.1
 */
public class Check1688API {
    private String productID;
    public String statusResult = null;
    public String shortResult = "";
    private int appIndex = ConfigInfo.appIndex;
    private Switch1688KeyIndex switch1688KeyIndex = Switch1688KeyIndex.getInstance();

    public Check1688API(String productUrl) {
        this.productID = Common.getProductID(productUrl);
        if (productID == Common.URL_ERROR) {
            statusResult = Common.URL_ERROR;
            shortResult = "";
        } else {
            getStatusResult();
            if (statusResult == Common.API_MAX) {
                getStatusResult();// 如果达到上限则继续回调一下，如果还为上限则说明所有的key都达到上限了
            }
        }
    }

    private void getStatusResult() {
        SDKResult<AlibabaAgentProductSimpleGetResult> result = getAlibabaResult();
        if (result.getErrorCode() == null) {
            statusResult = getStatusResult(result);
            shortResult = getShortInSize(result);
        } else if (result.getErrorCode() == "403") {
            statusResult = Common.API_MAX;
            // 此时达到了API调用的上限，则需要切换key与secret的值
            switch1688KeyIndex.change1688AppIndex(appIndex);
            shortResult = null;
        } else {
            statusResult = null;
            shortResult = null;
        }
    }

    // 调用1688API
    private SDKResult<AlibabaAgentProductSimpleGetResult> getAlibabaResult() {
        ApiExecutor apiExecutor = new ApiExecutor(ConfigInfo.appKeyArr[appIndex],
                ConfigInfo.appSecretArr[appIndex]);
        AlibabaAgentProductSimpleGetParam param = new AlibabaAgentProductSimpleGetParam();
        param.setWebSite(ConfigInfo.WebSite);
        param.setProductID(Long.valueOf(productID));
        SDKResult<AlibabaAgentProductSimpleGetResult> result = apiExecutor.execute(param);
        return result;
    }

    public String[] process() {
        String[] result = { statusResult, shortResult };
        return result;
    }

    // 得到status的结果
    public String getStatusResult(SDKResult<AlibabaAgentProductSimpleGetResult> result) {
        String message = "";
        try {
            try {
                message = result.getResult().getErrMsg();
            } catch (Exception ee) {
                message = null;
            }
            if (message == null || message.length() == 0) {
                String stutus = result.getResult().getProductInfo().getStatus();
                if (stutus.equals("published")) {
                    return Common.RES_PRODUCT_EXIST;
                } else {
                    return Common.RES_PRODUCT_INVALID;
                }
            } else {
                return Common.RES_PRODUCT_INVALID;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // 找断码
    public String getShortInSize(SDKResult<AlibabaAgentProductSimpleGetResult> result) {
        String shortResult = "";
        try {
            AlibabaProductProductInfo alibabaProductProductInfo = result.getResult()
                    .getProductInfo();
            AlibabaProductProductSKUInfo[] SKUInfoArr = alibabaProductProductInfo.getSkuInfos();
            for (AlibabaProductProductSKUInfo SKUInfo : SKUInfoArr) {
                int amountOnSale = 0;
                String singleResult = "";
                try {
                    amountOnSale = SKUInfo.getAmountOnSale();
                } finally {
                    if (amountOnSale == 0) {
                        for (AlibabaProductSKUAttrInfo attribute : SKUInfo.getAttributes()) {
                            singleResult = String.join("_", singleResult,
                                    attribute.getAttributeValue());
                        }
                        singleResult = singleResult.replaceFirst("_", "");
                        shortResult = String.join(",", shortResult, singleResult);
                    }
                }
            }
            shortResult = shortResult.replaceFirst(",", "");
        } catch (Exception e) {
            System.out.println("获取json结果异常" + result);
            return shortResult = null;
        }
        return shortResult;
    }

    public static void main(String[] args) {

    }

}
