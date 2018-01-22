package com.mode.util;

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

    public static void main(String[] args) {
        System.out.println(parseProductName("Centre Parting Ombre Wine Bob Short Hair Wig"));
        System.out.println(parseProductName("Yellow Embroidered Cold Shoulder Dress - S"));
        System.out.println(parseProductName("Hair Claw Clip Straight Long Hair Ponytail Extensions - 2H33#"));
        System.out.println(parseProductName("Halloween Eyeball &Tai Chi Print Socks - Pattern-1 / US 6 - US 8.5"));
    }
}
