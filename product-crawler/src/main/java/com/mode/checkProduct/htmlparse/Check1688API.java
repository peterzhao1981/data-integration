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

/*
 * @author maxiaodong on 2018/05/17
 * @version 0.0.1
 */
public class Check1688API {
    private String productID;
    public String statusResult = null;
    public String shortResult = "";

    public Check1688API(String productUrl) {
        this.productID = Common.getProductID(productUrl);
        if (productID == Common.URL_ERROR) {
            statusResult = Common.URL_ERROR;
            shortResult = "";
        } else {
            SDKResult<AlibabaAgentProductSimpleGetResult> result = getAlibabaResult();
            System.out.println(
                    "resultErrorCode:" + result.getErrorCode() + ",productID:" + productID);
            if (result.getErrorCode() == null) {
                statusResult = getStatusResult(result);
                shortResult = getShortInSize(result);
            } else if (result.getErrorCode() == "403") {
                statusResult = Common.API_MAX;
                shortResult = null;
            } else {
                statusResult = null;
                shortResult = null;
            }

        }
    }

    // 调用1688API
    private SDKResult<AlibabaAgentProductSimpleGetResult> getAlibabaResult() {
        ApiExecutor apiExecutor = new ApiExecutor("8037007", "lEWaInEqvizJ");// (ConfigInfo.appKey,
                                                                             // ConfigInfo.appSecret);
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
        String message = null;
        try {
            message = result.getErrorCode();
            if (message == null) {
                message = result.getResult().getErrMsg();
                if (message == null) {
                    String stutus = result.getResult().getProductInfo().getStatus();
                    if (stutus.equals("published")) {
                        String aaa = getShortInSize(result);
                        System.out.println(aaa);
                        return Common.RES_PRODUCT_EXIST;
                    } else {
                        return Common.RES_PRODUCT_INVALID;
                    }
                } else {
                    return Common.RES_PRODUCT_INVALID;
                }
            } else {
                // gw.QosAPIFrequencyLimit为API调用超限提示
                return Common.API_MAX;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Common.API_MAX;
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
        } catch (

        Exception e) {
            // TODO: handle exception
            System.out.println("获取json结果异常");
            return shortResult;
        }
        return shortResult;
    }

    public static void main(String[] args) {
        Check1688API check1688api = new Check1688API(
                "https://detail.1688.com/offer/542195921671.html?spm=a2615.7691456.0.0.1c0075daty6DnL");
        String[] aa = check1688api.process();
        System.out.println(aa[1]);
        System.out.println(aa[0]);
    }

}
