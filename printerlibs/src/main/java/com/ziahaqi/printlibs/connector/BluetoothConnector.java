package com.ziahaqi.printlibs.connector;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.ziahaqi.printlibs.builder.BluetoothBuilder;
import com.ziahaqi.printlibs.exception.PrinterException;
import com.ziahaqi.printlibs.factory.PrinterConnector;
import com.ziahaqi.printlibs.model.Printer;
import com.ziahaqi.printlibs.model.PrinterLabel;
import com.ziahaqi.printlibs.model.PrinterListener;
import com.ziahaqi.printlibs.model.PrinterType;
import com.ziahaqi.printlibs.utils.bluetooth.StringUtil;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.UUID;

/**
 * Created by zinux on 10/07/15.
 */
public class BluetoothConnector extends Printer implements PrinterConnector {
    private final BluetoothDevice device;
    private Context context;
    private PrinterListener listener;
    private BluetoothSocket mSocket;
    private OutputStream mOutputStream;
    private static final String TAG = "P25";
    private static  String SPP_UUID = "00001101-0000-1000-8000-00805F9B34FB";
    private boolean mIsConnecting = false;
    private UUID uuids ;
    private ConnectionTask connectionTask;

    public BluetoothConnector(BluetoothBuilder builder) {
        this.context = builder.getContext();
        this.listener = builder.getPrinterListener();
        this.device = builder.getDevice();
        this.label = builder.getPrinterLabel();
        this.macAddress = builder.getMacAddress();
        this.name = builder.getName();
        this.printerId = builder.getPrinterId();
        this.printerType = builder.getPrinterType();
        this.unit = builder.getPrinterUnit();
        Log.i(TAG, "libname>BluetoothConnector:" + builder.getName());

    }


    @Override
    public void connect() throws PrinterException {
        this.name = device.getName();
        if (mIsConnecting && connectionTask != null) {
            throw new PrinterException("Connection in progress");
        }
        if (mSocket != null) {
            throw new PrinterException("Socket already connected");
        }
        connectionTask =  new ConnectionTask(device);
        connectionTask.execute();
    }

    @Override
    public void disconnect() throws PrinterException {
        if (mSocket == null) {
            throw new PrinterException("Socket is not connected");
        }
        try {
            mSocket.close();
            mSocket = null;
            listener.onDisconnected(name, printerType);
        } catch (IOException e) {
            throw new PrinterException(e.getMessage());
        }
    }
    @Override
    public void cancelConnection() throws PrinterException {
        if (mIsConnecting && connectionTask != null) {
            connectionTask.cancel(true);

            listener.onConnectionCancelled(name, printerType);
        } else {
            throw new PrinterException("No connection is in progress");
        }
    }

    public void sendData(byte[] msg) throws PrinterException {
        if (mSocket == null) {
            throw new PrinterException("Socket is not connected, try to call connect() first");
        }

        try {
            mOutputStream.write(msg);
            mOutputStream.flush();

            Log.i(TAG, StringUtil.byteToString(msg));
        } catch(Exception e) {
            throw new PrinterException(e.getMessage());
        }
    }


    @Override
    public boolean isConnecting() {
        return mIsConnecting;
    }

    @Override
    public boolean isConnected() {
        return (mSocket != null);
    }


    private class ConnectionTask extends AsyncTask<URL, Integer, Long>{
        private BluetoothDevice device;
        private String error = "";

        private ConnectionTask(BluetoothDevice device) {
            this.device = device;
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            mIsConnecting = false;
            listener.onConnectionCancelled(name, printerType);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            listener.onStartConnecting(getPrinterId());
            mIsConnecting = true;
        }

        @Override
        protected void onCancelled(Long aLong) {
            mSocket = null;
            super.onCancelled(aLong);
        }

        @Override
        protected Long doInBackground(URL... urls) {
            long result = 0;

            try {
                mSocket	= device.createRfcommSocketToServiceRecord(UUID.fromString(SPP_UUID));

                mSocket.connect();

                mOutputStream	= mSocket.getOutputStream();

                result = 1;
            } catch (IOException e) {
                e.printStackTrace();
                error = e.getMessage();
                try {
                    Method m = device.getClass().getMethod("createRfcommSocket", new Class[] {int.class});
                    mSocket = (BluetoothSocket) m.invoke(device, 1);
                    mSocket.connect();
                }catch (Exception er){

                }
            }

            return result;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(Long result) {
            super.onPostExecute(result);
            mIsConnecting = false;

            if (mSocket != null && result == 1) {
                listener.onConnectionSuccess(name, printerType);
            } else {
                if(mSocket != null){
                    try {
                        error = "printer is not activated";
                        mSocket.close();
                        mSocket = null;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                listener.onConnectionFailed(name, printerType, "Connection failed " + error);
            }
        }
    }

}
