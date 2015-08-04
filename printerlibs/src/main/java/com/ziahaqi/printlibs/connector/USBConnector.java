package com.ziahaqi.printlibs.connector;

import android.util.Log;

import com.ziahaqi.printlibs.builder.USBBuilder;
import com.ziahaqi.printlibs.exception.PrinterException;
import com.ziahaqi.printlibs.factory.PrinterConnector;
import com.ziahaqi.printlibs.model.Printer;

/**
 * Created by zinux on 23/07/15.
 */
public class USBConnector extends Printer implements PrinterConnector {
    private final String TAG = "USBConnector";
    private String name;

    public USBConnector(USBBuilder builder) {
        this.name = builder.getName();
    }

    @Override
    public void connect() throws PrinterException {
        Log.i(TAG, name + "connect");

    }

    @Override
    public void disconnect() {
        Log.i(TAG, name + "disconnect");

    }

    @Override
    public void cancelConnection() throws PrinterException {

    }

    @Override
    public boolean isConnecting() {
        Log.i(TAG, name + "isConnecting");

        return false;
    }

    @Override
    public boolean isConnected() {
        Log.i(TAG, name + "isConnected");

        return false;
    }
}
