package com.ziahaqi.sample;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.ziahaqi.printlibs.PrinterActivity;
import com.ziahaqi.printlibs.exception.PrinterException;
import com.ziahaqi.printlibs.factory.PrinterConnector;
import com.ziahaqi.printlibs.model.ConnectionType;


public class MainActivity extends PrinterActivity {
    private Button mConnectBtn, mCreateBtn, mDisconnectBtn, mCancelBtn;
    private Button mEnableBtn;
    private Button mPrintDemoBtn;
    private Button mPrintBarcodeBtn;
    private Button mPrintImageBtn;
    private Button mPrintReceiptBtn;
    private Button mPrintTextBtn;
    private Spinner mDeviceSp;

    private ProgressDialog mProgressDlg;
    private ProgressDialog mConnectingDlg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mConnectBtn			= (Button) findViewById(R.id.btn_connect);
        mEnableBtn			= (Button) findViewById(R.id.btn_enable);
        mPrintDemoBtn 		= (Button) findViewById(R.id.btn_print_demo);
        mPrintBarcodeBtn 	= (Button) findViewById(R.id.btn_print_barcode);
        mPrintImageBtn 		= (Button) findViewById(R.id.btn_print_image);
        mPrintReceiptBtn 	= (Button) findViewById(R.id.btn_print_receipt);
        mPrintTextBtn		= (Button) findViewById(R.id.btn_print_text);
        mDeviceSp 			= (Spinner) findViewById(R.id.sp_device);
        mDisconnectBtn = (Button)findViewById(R.id.btn_disconnect);
        mCancelBtn = (Button)findViewById(R.id.btn_cancel);

        mCreateBtn = (Button)findViewById(R.id.btn_create);

        mDisconnectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    disconnect(ConnectionType.BLUETOOTH);
                } catch (PrinterException e) {
                    Toast.makeText(MainActivity.this, e.getError(), Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        });

        mCancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    cancelConnection(ConnectionType.BLUETOOTH);
                } catch (PrinterException e) {
                    Toast.makeText(MainActivity.this, e.getError(), Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        });

        mCreateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BluetoothDevice device = mBluetoothDeviceList.get(mDeviceSp.getSelectedItemPosition());
                try {
                    createBluetoothConnection(device);
                } catch (PrinterException e) {
                    Toast.makeText(MainActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        });

        mPrintTextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(PrinterConnector connector : mConnectorList){
                    String sampleText = " string text ";
                    try {
                        connector.sendData(sampleText.getBytes());
                    } catch (PrinterException e) {
                        Log.e("print", e.getMessage());
                        Toast.makeText(getApplicationContext(), "printer:" + connector.getName(), Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                }
            }
        });

        if (mBluetoothAdapter == null) {
//            showUnsupported();
        } else {
            if (!mBluetoothAdapter.isEnabled()) {
                showDisabled();
            }else {
                showEnabled();
                updateBluetoothDeviceList();

            }

            mProgressDlg 	= new ProgressDialog(this);

            mProgressDlg.setMessage("Scanning...");
            mProgressDlg.setCancelable(false);
            mProgressDlg.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();

                    mBluetoothAdapter.cancelDiscovery();
                }
            });

            mConnectingDlg 	= new ProgressDialog(this);

            mConnectingDlg.setMessage("Connecting...");
            mConnectingDlg.setCancelable(false);

            //enable bluetooth
            mEnableBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);

                    startActivityForResult(intent, 1000);
                }
            });

            //connect/disconnect
            mConnectBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    try {
                        connect(null);
                    } catch (PrinterException e) {
                        Toast.makeText(MainActivity.this, e.getError(), Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                }
            });

        }
    }


    private void updateBluetoothDeviceList() {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.simple_spinner_item, getBlueetoothNames(mBluetoothDeviceList));
        adapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
        mDeviceSp.setAdapter(adapter);
        mDeviceSp.setSelection(0);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onConnectionCancelled(String name, ConnectionType connectionType) {
        Log.i("bluetooth", "onConnectionCancelled()");
    }

    @Override
    public void onConnectionSuccess(String name, ConnectionType connectionType) {
        Log.i("bluetooth", "onConnectionSuccess()");
    }

    @Override
    public void onConnectionFailed(String name, ConnectionType connectionType, String error) {
        Log.i("bluetooth", "onConnectionFailed()");
    }

    @Override
    public void onDisconnected(String name, ConnectionType connectionType) {
        Log.i("bluetooth", "onDisconnected()");
    }

    @Override
    public void onStartConnecting(String printerId) {
        Log.i("bluetooth", "onStartConnecting()");
    }

    @Override
    protected void onBluetoothStateOn() {
        showEnabled();
    }

    @Override
    protected void onBluetoothStateOff() {
        showDisabled();
    }

    @Override
    protected void onBluetoothDiscoveryFinished() {
        super.onBluetoothDiscoveryFinished();
        updateBluetoothDeviceList();
    }

    private void showDisabled(){
        Toast.makeText(this, "Bluetooth disabled", Toast.LENGTH_SHORT).show();
        mEnableBtn.setVisibility(View.VISIBLE);
        mConnectBtn.setVisibility(View.GONE);
        mDeviceSp.setVisibility(View.GONE);
    }

    private void showEnabled(){
        Toast.makeText(this, "Bluetooth enabled", Toast.LENGTH_SHORT).show();
        mEnableBtn.setVisibility(View.GONE);
        mConnectBtn.setVisibility(View.VISIBLE);
        mDeviceSp.setVisibility(View.VISIBLE);
    }
}
