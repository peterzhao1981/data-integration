package com.mode.test;

import com.alibaba.ocean.rawsdk.ApiExecutor;
import com.alibaba.ocean.rawsdk.common.SDKResult;
import com.alibaba.product.param.AlibabaAgentProductSimpleGetParam;
import com.alibaba.product.param.AlibabaAgentProductSimpleGetResult;

public class TestApi {

    public static void main(String[] args) {
        // TODO Auto-generated method stub
        String appKey = "4503562";
        String appSecret = "18M7cOyesAEj";
        ApiExecutor apiExecutor = new ApiExecutor(appKey, appSecret);

        AlibabaAgentProductSimpleGetParam param = new AlibabaAgentProductSimpleGetParam();
        param.setWebSite("1688");
        param.setProductID(559414619819L);
        SDKResult<AlibabaAgentProductSimpleGetResult> result = apiExecutor.execute(param);
        if (result.getResult().getErrMsg() != null) {
            System.out.println(result.getErrorMessage());
        } else {
            String stutus = result.getResult().getProductInfo().getStatus();
            if (!stutus.equals("published")) {
                System.out.println(result.getResult().getProductInfo().getStatus() + "不存在");
            } else {
                System.out.println(result.getResult().getProductInfo().getStatus() + "存在");
            }

        }

    }

}
