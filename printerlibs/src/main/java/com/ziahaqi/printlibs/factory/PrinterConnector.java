package com.ziahaqi.printlibs.factory;

import com.ziahaqi.printlibs.exception.PrinterException;
import com.ziahaqi.printlibs.model.PrinterLabel;
import com.ziahaqi.printlibs.model.PrinterType;

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

    public PrinterType getPrinterType();

    public PrinterLabel getLabel();

    public String getName();

    public String getUnit();

}
