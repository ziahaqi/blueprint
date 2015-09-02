package com.ziahaqi.printlibs.builder;

import android.bluetooth.BluetoothDevice;
import android.content.Context;

import com.ziahaqi.printlibs.connector.BluetoothConnector;
import com.ziahaqi.printlibs.factory.PrinterConnector;
import com.ziahaqi.printlibs.model.PrinterType;
import com.ziahaqi.printlibs.model.PrinterListener;
import com.ziahaqi.printlibs.model.ConnectionType;

/**
 * Created by zinux on 23/07/15.
 */
public class BluetoothBuilder extends DriverBuilder {
    private BluetoothDevice device;
    private String macAddress;

    public BluetoothBuilder(Context context, BluetoothDevice device, PrinterType printerType, PrinterListener printerListener) {
        this.context = context;
        this.device = device;
        this.connectionType = ConnectionType.BLUETOOTH;

        this.printerType = printerType;
        this.printerListener = printerListener;
        this.printerId = device.getAddress();

    }

    public BluetoothBuilder context(Context context){
        this.context = context;
        return this;
    }

    public BluetoothBuilder device(BluetoothDevice device){
        this.device = device;
        return this;
    }

    public BluetoothBuilder listener(PrinterListener listener){
        this.printerListener = listener;
        return this;
    }

    public BluetoothBuilder name(String name){
        this.name = name;
        return this;
    }

    public BluetoothBuilder macAddress(String macAddress){
        this.macAddress = macAddress;
        return this;
    }

    public BluetoothBuilder label(PrinterType printerType){
        this.printerType = printerType;
        return this;
    }

    public BluetoothBuilder type(ConnectionType connectionType){
        this.connectionType = connectionType;
        return this;
    }

    public BluetoothBuilder unit(String printerUnit){
        this.printerUnit = printerUnit;
        return this;
    }

    public BluetoothDevice getDevice() {
        return device;
    }

    public String getMacAddress() {
        return macAddress;
    }


    @Override
    public PrinterConnector build() {
        return new BluetoothConnector(this);
    }
}
