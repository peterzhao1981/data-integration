package com.mode.checkProduct.endtofront;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSONObject;
import com.mode.entity.CheckProductStatus;

//将后台数据，以json格式返回到前台
public class JSONData {
    public static void dealEndData(HttpServletResponse response, String userName, int page,
            int rows, List<CheckProductStatus> result) throws Exception {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter write = null;
        JSONObject json = new JSONObject();
        write = response.getWriter();
        CheckProductStatus checkProductStatus = null;
        List<JSONObject> list = new ArrayList<>();
        for (int i = (page - 1) * rows; i < page * rows; i++) {
            if (i >= result.size())
                break;
            checkProductStatus = result.get(i);
            JSONObject jsonRow = new JSONObject();
            try {
                jsonRow.put("id", checkProductStatus.getId());
                jsonRow.put("product_id", checkProductStatus.getProductId());
                jsonRow.put("product_url", checkProductStatus.getProductUrl());
                jsonRow.put("sku", checkProductStatus.getSku());
                jsonRow.put("spu", checkProductStatus.getSpu());
                jsonRow.put("status", checkProductStatus.getStatus());
                list.add(jsonRow);
            } catch (Exception e) {
            }
        }
        json.put("total", result.size());
        json.put("rows", list);
        // System.out.println(json.get("product_url"));
        write.print(json);
        write.flush();
        write.close();
    }
}
