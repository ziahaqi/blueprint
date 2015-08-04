package com.ziahaqi.printlibs.exception;

/**
 * Created by zinux on 13/07/15.
 */
public class PrinterException extends Exception{
    private static final long serialVersionUID = 1L;

    String error = "";

    public PrinterException(String msg) {
        super(msg);
        error = msg;
    }

    public String getError() {
        return error;
    }
}
