package com.ziahaqi.printlibs.builder;

import android.content.Context;

import com.ziahaqi.printlibs.factory.PrinterConnector;
import com.ziahaqi.printlibs.model.PrinterType;
import com.ziahaqi.printlibs.model.PrinterListener;
import com.ziahaqi.printlibs.model.ConnectionType;

/**
 * Created by zinux on 23/07/15.
 */
public abstract class DriverBuilder {
    protected String printerId;
    protected Context context;
    protected PrinterType printerType;
    protected ConnectionType connectionType;
    protected String printerUnit;
    protected PrinterListener printerListener;
    protected String name;

    public String getPrinterId() {
        return printerId;
    }

    public Context getContext() {
        return context;
    }

    public PrinterType getPrinterType() {
        return printerType;
    }

    public ConnectionType getConnectionType() {
        return connectionType;
    }

    public String getPrinterUnit() {
        return printerUnit;
    }

    public PrinterListener getPrinterListener() {
        return printerListener;
    }

    public String getName() {
        return name;
    }

    public abstract  PrinterConnector build();
}
