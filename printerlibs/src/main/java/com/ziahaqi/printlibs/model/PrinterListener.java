package com.ziahaqi.printlibs.model;

/**
 * Created by zinux on 13/07/15.
 */
public interface PrinterListener {
    public abstract void onConnectionCancelled(String name, ConnectionType connectionType);
    public abstract void onConnectionSuccess(String name, ConnectionType connectionType);
    public abstract void onConnectionFailed(String name, ConnectionType connectionType, String error);
    public abstract void onDisconnected(String name, ConnectionType connectionType);
    public abstract void  onStartConnecting(String printerId); 
}
