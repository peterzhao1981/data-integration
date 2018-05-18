package com.mode.test;

import java.util.EnumMap;
import java.util.Map;

public class AAA {

    public static void main(String[] args) {
        // TODO Auto-generated method stub
        EnumMap<ErrMsgEnum, String> errMsgMap = new EnumMap<ErrMsgEnum, String>(ErrMsgEnum.class);

        errMsgMap.put(ErrMsgEnum.required_item_id, "2,4,6");
        errMsgMap.put(ErrMsgEnum.invalid_app_id, "1,7");
        errMsgMap.put(ErrMsgEnum.invalid_date, "8");

        System.out.println(errMsgMap.get(ErrMsgEnum.invalid_date));

        for (Map.Entry<ErrMsgEnum, String> entry : errMsgMap.entrySet()) {
            System.out.println(entry.getValue() + " " + entry.getKey().getValue());
        }
    }

}

enum ErrMsgEnum {
    required_item_id("商品id为必填项"), invalid_app_id("应用标识错误"), invalid_date("时间格式错误");

    private String value;

    private ErrMsgEnum(String value) {
        this.setValue(value);
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

}
