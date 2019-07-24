package com.example.bluetoothtest;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.text_view_1) TextView mTextView1;
    @BindView(R.id.text_view_2) TextView mTextView2;
//    @BindView(R.id.button_1) Button mButton1;

    BluetoothAdapter mBluetoothAdapter;

    private static final int REQUEST_ENABLE_BT = 1;
    private BroadcastReceiver mReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    private void init(){
        // Create a BroadcastReceiver for ACTION_FOUND
        mReceiver = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                // When discovery finds a device
                if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                    // Get the BluetoothDevice object from the Intent
                    BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    // Add the name and address to an array adapter to show in a ListView
//                    mArrayAdapter.add(device.getName() + "\n" + device.getAddress());
                    mTextView2.append(device.getName()+"\t"+device.getAddress()+"\n");
                }
            }
        };
        // Register the BroadcastReceiver
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(mReceiver, filter); // Don't forget to unregister during onDestroy
    }

    @OnClick(R.id.button_1)
    public void BluetoothSetting(){
       mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
       if (mBluetoothAdapter != null){
           if (mBluetoothAdapter.isEnabled()){
               Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
               startActivityForResult(enableBtIntent,REQUEST_ENABLE_BT);
           }else {
               Toast.makeText(MainActivity.this,"蓝牙设备可用",Toast.LENGTH_SHORT).show();
           }
       }else {
           Toast.makeText(MainActivity.this,"为空",Toast.LENGTH_SHORT).show();
       }
    }

    @OnClick(R.id.button_2)
    public void checkPairedDevices(){
        Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
        if (pairedDevices == null & pairedDevices.size() > 0){
            for (BluetoothDevice device : pairedDevices){
                mTextView1.append(device.getName()+"\t");
            }
        }else {
            Toast.makeText(MainActivity.this,"没有已配对设备",Toast.LENGTH_SHORT).show();
        }
    }

    @OnClick(R.id.button_3)
    public void FindDevice(){
        
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch (requestCode){
            case REQUEST_ENABLE_BT:
                if (resultCode == RESULT_OK){
                    Toast.makeText(MainActivity.this,"蓝牙设备可用",Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}
