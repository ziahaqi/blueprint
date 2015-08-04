package com.ziahaqi.printlibs.model;

/**
 * Created by zinux on 13/07/15.
 */
public interface PrinterListener {
    public abstract void onConnectionCancelled(String name, PrinterType printerType);
    public abstract void onConnectionSuccess(String name, PrinterType printerType);
    public abstract void onConnectionFailed(String name, PrinterType printerType, String error);
    public abstract void onDisconnected(String name, PrinterType printerType);
    public abstract void  onStartConnecting(String printerId);
}
