package com.ziahaqi.printlibs.builder;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;

import com.ziahaqi.printlibs.connector.USBConnector;
import com.ziahaqi.printlibs.factory.PrinterConnector;
import com.ziahaqi.printlibs.model.PrinterListener;

/**
 * Created by zinux on 23/07/15.
 */
public class USBBuilder extends DriverBuilder {
    private Context context;
    private BluetoothDevice device;
    private BluetoothAdapter adapter;
    private PrinterListener listener;
    private String name;
    private String macAddress;

    public USBBuilder(Context context, BluetoothDevice device, BluetoothAdapter adapter, PrinterListener listener) {
        this.context = context;
        this.device = device;
        this.adapter = adapter;
        this.listener = listener;
    }

    public USBBuilder context(Context context){
        this.context = context;
        return this;
    }

    public USBBuilder device(BluetoothDevice device){
        this.device = device;
        return this;
    }

    public USBBuilder adapter(BluetoothAdapter adapter){
        this.adapter = adapter;
        return this;
    }

    public USBBuilder listener(PrinterListener listener){
        this.listener = listener;
        return this;
    }

    public USBBuilder name(String name){
        this.name = name;
        return this;
    }

    public USBBuilder macAddress(String macAddress){
        this.macAddress = macAddress;
        return this;
    }

    public Context getContext() {
        return context;
    }

    public BluetoothDevice getDevice() {
        return device;
    }

    public BluetoothAdapter getAdapter() {
        return adapter;
    }

    public PrinterListener getListener() {
        return listener;
    }

    public String getName() {
        return name;
    }

    public String getMacAddress() {
        return macAddress;
    }
    @Override
    public PrinterConnector build() {
        return new USBConnector(this);
    }
}
