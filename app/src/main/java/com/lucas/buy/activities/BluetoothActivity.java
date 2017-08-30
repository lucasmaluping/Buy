package com.lucas.buy.activities;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.lucas.buy.R;

import java.util.ArrayList;
import java.util.Set;

/**
 * Created by malucas on 30/08/2017.
 */

public class BluetoothActivity extends BaseActivity {
    private static final String TAG = "BluetoothActivity";
    private Button On,Off,Visible,list;
    private BluetoothAdapter bluetoothAdapter;
    private Set<BluetoothDevice> pairedDevices;
    private ListView lv;
    private int connectState;
    private static final String name = "红米手机";

    @Override
    protected void handler(Message msg) {

    }

    @Override
    protected void initContentView(Bundle savedInstanceState) {
        setContentView(R.layout.bluetooth_layout);

        On = (Button)findViewById(R.id.button1);
        Off = (Button)findViewById(R.id.button2);
        Visible = (Button)findViewById(R.id.button3);
        list = (Button)findViewById(R.id.button4);
        lv = (ListView)findViewById(R.id.listView1);

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
            int permissioncheck = 0;
            permissioncheck = this.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION);
            permissioncheck += this.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION);
            if (permissioncheck != PackageManager.PERMISSION_GRANTED) {
                //注册权限
                this.requestPermissions(new String[] {Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.BLUETOOTH_ADMIN, Manifest.permission.BLUETOOTH}, 0);
            } else {
                //已经获得过权限....
            }
        }
    }

    public void on(View view) {
        if(!bluetoothAdapter.isEnabled()) {
            Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(intent, 0);
            Toast.makeText(this, "bluetooth turn on", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "bluetooth already on", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 获得已经配对的蓝夜设备
     * @param view
     */
    public void list(View view) {
        Set<BluetoothDevice> bondedDevices = bluetoothAdapter.getBondedDevices();
        ArrayList<String> list = new ArrayList<>();
        for(BluetoothDevice bd : bondedDevices) {
            list.add(bd.getName());
        }

        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, list);
        lv.setAdapter(adapter);
    }

    /**
     * 关闭蓝牙
     * @param view
     */
    public void off(View view) {
        bluetoothAdapter.disable();
        Toast.makeText(this, "bluetooth turn off", Toast.LENGTH_SHORT).show();
    }

    public void visible(View view) {
        Intent getVisible = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        startActivityForResult(getVisible, 0);
    }

    public void find(View view) {
        IntentFilter intentFilter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(mReceiver, intentFilter);
        intentFilter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        registerReceiver(mReceiver, intentFilter);
        bluetoothAdapter.startDiscovery();
    }

    BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Log.i(TAG,"....action:" + action);
            //
            if(BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
//                Log.i(TAG, "....parcelableExtra:" + parcelableExtra);
                Log.i(TAG,"....device:" + device.getName());
                if (device.getName() != null && device.getName().equalsIgnoreCase(name)) {
                    bluetoothAdapter.cancelDiscovery();
                    connectState = device.getBondState();
                    Log.i(TAG,"....connectedState:" + connectState);
                    switch (connectState) {
                        case BluetoothDevice.BOND_NONE:

                            break;
                        case BluetoothDevice.BOND_BONDED:

                            break;
                    }
                }
            }
        }
    };

}






















