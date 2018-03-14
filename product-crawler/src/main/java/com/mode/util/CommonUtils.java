package com.mode.util;

import org.springframework.util.*;

/**
 * Created by zhaoweiwei on 2017/11/30.
 */
public class CommonUtils {

    public static String parseProductName(String skuName) {
        int index = skuName.lastIndexOf(" / ");
        if (index != -1) {
            String base = skuName.substring(0, index);
            index = base.lastIndexOf(" - ");
            if (index != -1) {
                return base.substring(0, index);
            } else {
                return skuName;
            }
        } else {
            index = skuName.lastIndexOf(" - ");
            if (index != -1) {
                return skuName.substring(0, index);
            } else {
                return skuName;
            }
        }
    }

    public static String skuConverter(String sku) {
        if (!org.springframework.util.StringUtils.isEmpty(sku)) {
            sku = sku.replaceAll(" ", "").replaceAll("&", "").replaceAll("\\+", "").replaceAll("/", "");
            if (sku.endsWith("#")) {
                sku = sku.substring(0, sku.length() - 1);
            }
        }
        return sku;
    }


    public static void main(String[] args) {
//        System.out.println(parseProductName("Centre Parting Ombre Wine Bob Short Hair Wig"));
//        System.out.println(parseProductName("Yellow Embroidered Cold Shoulder Dress - S"));
//        System.out.println(parseProductName("Hair Claw Clip Straight Long Hair Ponytail Extensions - 2H33#"));
//        System.out.println(parseProductName("Halloween Eyeball &Tai Chi Print Socks - Pattern-1 / US 6 - US 8.5"));

        System.out.println(skuConverter("p12411-5528-Laser-iPhone6/6S(4.7)"));
//        System.out.println("\\");
    }
}
