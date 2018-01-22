package com.mode.util;

import java.util.HashMap;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

/**
 * Created by zhaoweiwei on 2017/12/25.
 */
public class Test {

    public static void main(String[] args) {
//        String url = "https://www.dianxiaomi.com/dxmSupplierProductRelation/supplierRelationList.htm";
//        String url = "https://www.dianxiaomi.com/dxmSupplierProductRelation/supplierRelationPageList.htm";
        String url = "https://www.dianxiaomi.com/dxmSupplierProductRelation/supplierProductRelationList" +
                ".htm?supplierId=14476857286570040";

        Document doc;
        long now = System.currentTimeMillis();
        Map<String, String> cookies = new HashMap<String, String>();
        cookies.put("dxm_vc", "ZDBjYWJkYWFkMzIyOGFlNzUzY2I1YWRhMjNjMDI3MTYhMTUxMzg1MjUyNTUzMg");
        cookies.put("dxm_i", "MjEyMDI5IWFUMHlNVEl3TWprITdiOTQxN2E5ZjFiYWE2M2Y0NmUyOWVhYjkxOWE3NTI1");
        cookies.put("dxm_t", "MTUxMzg1MjU1NSFkRDB4TlRFek9EVXlOVFUxIWUxZTk1Yjk3YWY0NTk0NWQ5Y2FiMDM4MjJjMmE0YzEz");
        cookies.put("dxm_c", "TEwwS2g2T0EhWXoxTVREQkxhRFpQUVEhYTYyZTk5ZjNlMTI2ZDg4MDU2NDNiM2Y5NzNkYjUyZWU");
        cookies.put("dxm_w", "ZmQ5YmI1MmFlOGU4OTZiZmY3ZTcxNmRjYzUzYjJiMDEhZHoxbVpEbGlZalV5WVdVNFpUZzVObUptWmpkbE56RTJaR05qTlROaU1tSXdNUSEyZjZjNGYxZGVjMTI1Yzc1NjVlZjk3Y2E5ZDM5NTE1NQ");
        cookies.put("dxm_s", "KqkJ03xdBSPXgOIkv3GxY5q4FcR8OWPN9lepGMg6q04");
        try {
            doc = Jsoup.connect(url)
                    .data("pageNo", "1")
                    .data("pageSize", "50")
                    .data("searchType", "1")
                    .validateTLSCertificates(false).cookies(cookies).get();
            System.out.println(doc);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
