package com.mode.common.exception;

public class FormatCovertException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    /*
     * 错误编码
     */
    private String errorCode;

    /*
     * 构造一个基本异常.
     *
     * @param message 信息描述
     */
    public FormatCovertException(String message) {
        super(message);
    }

    public FormatCovertException(String errorCode, String message) {
        this(errorCode, message, true);
    }

    public FormatCovertException(String errorCode, String message, boolean propertiesKey) {
        super(message);
        this.setErrorCode(errorCode);
        this.setPropertiesKey(propertiesKey);
    }

    private void setPropertiesKey(boolean propertiesKey) {
        // TODO Auto-generated method stub

    }

    private void setErrorCode(String errorCode2) {
        // TODO Auto-generated method stub

    }

}
