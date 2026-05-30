package com.shayankhanani.connexio.exception;

public class UnauthorizedException extends RuntimeException {
    public UnauthorizedException(String messgae) {
        super(messgae);
    }
}
