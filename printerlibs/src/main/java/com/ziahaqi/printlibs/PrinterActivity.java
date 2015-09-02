package com.ziahaqi.printlibs;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.Toast;

import com.ziahaqi.printlibs.builder.BluetoothBuilder;
import com.ziahaqi.printlibs.exception.PrinterException;
import com.ziahaqi.printlibs.factory.PrinterConnector;
import com.ziahaqi.printlibs.factory.PrinterFactory;
import com.ziahaqi.printlibs.model.PrinterType;
import com.ziahaqi.printlibs.model.PrinterListener;
import com.ziahaqi.printlibs.model.ConnectionType;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by zinux on 03/08/15.
 */
public abstract class PrinterActivity extends FragmentActivity implements PrinterListener{
    private final String TAG = "PrinterActivity";

    /*
     * bluetooth property
     */
    protected List<PrinterConnector> mConnectorList;
    protected ArrayList<BluetoothDevice> mBluetoothDeviceList;
    protected BluetoothAdapter mBluetoothAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initBluetoothStuff();
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(mReceiver);
        super.onDestroy();
    }


    protected PrinterConnector findPrinterConnectorById(String printerId){
        if(mConnectorList != null){
            for(PrinterConnector connector : mConnectorList){
                if(connector.getPrinterId().equals(printerId)){
                    return connector;
                }
            }
        }
        return null;
    }

    protected PrinterConnector findPrinterConnectorByConnectionType(ConnectionType connectionType){
        if(mConnectorList != null){
            for(PrinterConnector connector : mConnectorList){
                if(connector.getConnectionType().equals(connectionType)){
                    return connector;
                }
            }
        }
        return null;
    }

    protected void addConnector(PrinterConnector connector){
        if(mConnectorList != null){
            mConnectorList.add(connector);
        }
    }

    /*
     * Bluetooth stuff
     */

    private void initBluetoothStuff(){
        if(mConnectorList == null){
            mConnectorList = new ArrayList<>();
        }
        if(mBluetoothAdapter == null){
            mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        }
        if(mBluetoothDeviceList == null){
            mBluetoothDeviceList = new ArrayList<>();
        }
        initBondedBluetoothDevices();
        Log.i(TAG, "oncreate()>regreciver");
        IntentFilter filter = new IntentFilter();
        filter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        filter.addAction(BluetoothDevice.ACTION_FOUND);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        filter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
        registerReceiver(mReceiver, filter);
    }

    protected void  initBondedBluetoothDevices(){
        Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
        if (pairedDevices != null) {
            mBluetoothDeviceList.clear();
            mBluetoothDeviceList.addAll(pairedDevices);
        }
        Log.i("devices", "size:" + mBluetoothDeviceList.size());
    }

    private BluetoothDevice createBondBluetoothDevice(BluetoothDevice device){
        if (device.getBondState() == BluetoothDevice.BOND_NONE) {
            try {
                Class<?> cl 	= Class.forName("android.bluetooth.BluetoothDevice");
                Class<?>[] par 	= {};

                Method method 	= cl.getMethod("createBond", par);

                method.invoke(device);
            } catch (Exception e) {
                Toast.makeText(this, "Failed To Pair device", Toast.LENGTH_SHORT).show();
            }
        }
        return device;
    }

    /*
     * Bluetooth
     */
    protected void createBluetoothConnection(BluetoothDevice device) throws PrinterException {
        if(device == null){
            throw new PrinterException("device or adapter maybe null");
        }
        PrinterConnector connector = findPrinterConnectorById(device.getAddress());
        if(connector == null){
            BluetoothDevice bondDevice = createBondBluetoothDevice(device);
            connector = PrinterFactory.init(new BluetoothBuilder(this, bondDevice, PrinterType.THERMAL, this)
                    .name("blue bambu")
                    .unit("kasir")).build();
            addConnector(connector);
        }else{
            Log.i(TAG, "object connector already exist");
        }
    }

    protected void connect(ConnectionType connectionType) throws PrinterException {
        if(connectionType == null){
            return;
        }
        PrinterConnector connector = findPrinterConnectorByConnectionType(connectionType);
        if(connector != null){
            connector.connect();
        }
    }

    protected void disconnect(ConnectionType connectionType) throws PrinterException {
        if(connectionType == null){
            return;
        }
        PrinterConnector connector = findPrinterConnectorByConnectionType(connectionType);
        if(connector != null){
            connector.disconnect();
        }
    }

    protected void cancelConnection(ConnectionType connectionType) throws PrinterException {
        if(connectionType == null){
            return;
        }
        PrinterConnector connector = findPrinterConnectorByConnectionType(connectionType);
        if(connector != null){
            connector.cancelConnection();
        }
    }

    //receiver
    protected final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            if (BluetoothAdapter.ACTION_STATE_CHANGED.equals(action)) {
                final int state 	= intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR);
                if (state == BluetoothAdapter.STATE_ON) {
                    onBluetoothStateOn();
                } else if (state == BluetoothAdapter.STATE_OFF) {
                    onBluetoothStateOff();
                }
            } else if (BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action)) {
                Log.d(TAG, "blutut:dicovery start");
                mBluetoothDeviceList = new ArrayList<>();
                onBluetoothDiscoveryStarted();
            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                Log.d(TAG, "blutut:dicovery finished");
                onBluetoothDiscoveryFinished();
            } else if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = (BluetoothDevice) intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                mBluetoothDeviceList.add(device);
            } else if (BluetoothDevice.ACTION_BOND_STATE_CHANGED.equals(action)) {
                final int state = intent.getIntExtra(BluetoothDevice.EXTRA_BOND_STATE, BluetoothDevice.ERROR);

                if (state == BluetoothDevice.BOND_BONDED) {
                    //   showToast("Paired");
                    //  connect();
                }
            }
        }
    };

    //
    protected String[] getBlueetoothNames(ArrayList<BluetoothDevice> data) {

        String[] list = new String[0];

        if (data == null) return list;

        int size	= data.size();
        list		= new String[size];

        for (int i = 0; i < size; i++) {
            list[i] = data.get(i).getName();
        }

        return list;
    }
    //
    protected  void onBluetoothStateOn(){
    }
    protected  void onBluetoothStateOff(){

    }
    protected  void onBluetoothDiscoveryStarted(){

    }
    protected  void onBluetoothDiscoveryFinished(){
        initBondedBluetoothDevices();
    }

}
