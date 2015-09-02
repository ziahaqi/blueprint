package com.ziahaqi.printlibs.factory;

import com.ziahaqi.printlibs.exception.PrinterException;
import com.ziahaqi.printlibs.model.PrinterType;
import com.ziahaqi.printlibs.model.ConnectionType;

/**
 * Created by zinux on 10/07/15.
 */
public interface PrinterConnector {

    public abstract void connect() throws PrinterException;

    public abstract void disconnect() throws PrinterException;

    public abstract void cancelConnection() throws PrinterException;

    public boolean isConnecting();

    public boolean isConnected();

    public String getPrinterId();

    public ConnectionType getPrinterType();

    public PrinterType getLabel();

    public String getName();

    public String getUnit();

    public abstract void sendData(byte[] bytes)throws PrinterException;

    ConnectionType getConnectionType();
}
