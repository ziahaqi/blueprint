package com.ziahaqi.printlibs.builder;

import android.content.Context;

import com.ziahaqi.printlibs.factory.PrinterConnector;
import com.ziahaqi.printlibs.model.PrinterLabel;
import com.ziahaqi.printlibs.model.PrinterListener;
import com.ziahaqi.printlibs.model.PrinterType;

/**
 * Created by zinux on 23/07/15.
 */
public abstract class DriverBuilder {
    protected String printerId;
    protected Context context;
    protected PrinterLabel printerLabel;
    protected PrinterType printerType;
    protected String printerUnit;
    protected PrinterListener printerListener;
    protected String name;

    public String getPrinterId() {
        return printerId;
    }

    public Context getContext() {
        return context;
    }

    public PrinterLabel getPrinterLabel() {
        return printerLabel;
    }

    public PrinterType getPrinterType() {
        return printerType;
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
