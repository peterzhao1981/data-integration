package com.mode.checkProduct;

import java.io.IOException;

import com.alibaba.ocean.rawsdk.ApiExecutor;
import com.alibaba.ocean.rawsdk.common.SDKResult;
import com.alibaba.product.param.AlibabaAgentProductSimpleGetParam;
import com.alibaba.product.param.AlibabaAgentProductSimpleGetResult;
import com.mode.checkProduct.commoninfo.ConfigInfo;

/*
 * @author maxiaodong on 2018/05/17
 * @version 0.0.1
 */
public class Check1688API {
    // public final static String appKey = "4503562";
    // public final static String appSecret = "18M7cOyesAEj";

    private String productID;

    public Check1688API(String productUrl) {
        this.productID = Common.getProductID(productUrl);
    }

    // 调用1688API
    private String call1688API() {
        String message = null;
        ApiExecutor apiExecutor = new ApiExecutor(ConfigInfo.appKey, ConfigInfo.appSecret);
        AlibabaAgentProductSimpleGetParam param = new AlibabaAgentProductSimpleGetParam();
        param.setWebSite(ConfigInfo.WebSite);
        param.setProductID(Long.valueOf(productID));
        SDKResult<AlibabaAgentProductSimpleGetResult> result = apiExecutor.execute(param);
        try {
            message = result.getErrorCode();
            if (message == null) {
                message = result.getResult().getErrMsg();
                if (message == null) {
                    String stutus = result.getResult().getProductInfo().getStatus();
                    if (!stutus.equals("published")) {
                        return Common.RES_PRODUCT_INVALID;
                    } else {
                        return Common.RES_PRODUCT_EXIST;
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

    public String process() {
        // 如果url格式错误，无需继续下面的处理，直接返回
        if (productID.equals(Common.URL_ERROR)) {
            return Common.URL_ERROR;
        }
        // 调用1688API的代码
        return call1688API();
    }

    public static void main(String[] args) throws IOException {
        String aaa = TryCatchFinally.test();
        System.out.println(aaa + ";aaa");
    }

}

class TryCatchFinally {

    @SuppressWarnings("finally")
    public static final String test() {
        String t = "";
        int i;
        int[] aa = { 1, 2 };

        try {
            t = "try";
            i = 1 / 0;
            return t;
        } catch (Exception e) {
            t = "catch";
            System.out.println(e);
            return t;
        } finally {
            t = "finally";
            t = String.valueOf(t + "value");
            System.out.println(t);
        }
    }

}
