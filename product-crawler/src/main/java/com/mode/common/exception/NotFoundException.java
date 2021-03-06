package com.mode.common.exception;

/**
 * Created by Michael.Zhong on 17/2/8.
 */
public class NotFoundException extends RuntimeException {

    /**
     * Constructs a new runtime exception with {@code null} as its detail
     * message. The cause is not initialized, and may subsequently be
     * initialized by a call to {@link #initCause}.
     */
    public NotFoundException() {
        super();
    }

    /**
     * Constructs a new runtime exception with the specified detail message. The
     * cause is not initialized, and may subsequently be initialized by a call
     * to {@link #initCause}.
     *
     * @param message
     *            the detail message. The detail message is saved for later
     *            retrieval by the {@link #getMessage()} method.
     */
    public NotFoundException(String message) {
        // super("我的异常" + message);
        System.out.println("个人运行时异常为空");
    }
}
