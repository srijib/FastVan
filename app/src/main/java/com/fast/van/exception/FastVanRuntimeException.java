package com.fast.van.exception;

/**
 * Created by Amandeep Singh Bagli on 23/03/16.
 */
public class FastVanRuntimeException extends RuntimeException {
    public FastVanRuntimeException() {
    }

    public FastVanRuntimeException(String detailMessage) {
        super(detailMessage);
    }

    public FastVanRuntimeException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public FastVanRuntimeException(Throwable throwable) {
        super(throwable);
    }
}
