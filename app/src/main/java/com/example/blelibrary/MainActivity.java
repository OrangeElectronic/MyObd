package com.example.blelibrary;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.example.blelibrary.EventBus.SerchDevice;
import com.example.blelibrary.Server.BleServiceControl;
import com.example.blelibrary.Server.ScanDevice;

import org.greenrobot.eventbus.Subscribe;

import static com.example.blelibrary.Control.EXTRAS_DEVICE_ADDRESS;
import static com.example.blelibrary.Control.EXTRAS_DEVICE_NAME;

public class MainActivity extends AppCompatActivity {
    ScanDevice scan=new ScanDevice();
    BleServiceControl bleServiceControl=new BleServiceControl();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        scan.setmBluetoothAdapter(this);
//scan.mLeDevices呼叫裝置
    }
        @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if(grantResults[0]!=0){
                Toast.makeText(this, "請先打開定位系統", Toast.LENGTH_LONG).show();
                scan.initPermission(this);
            }else{scan.RequestPermission();}
        }
    }
    /////////////////找到裝置
@Subscribe
    public void Event(SerchDevice a){
        try{
            if(a.getDevic().getName().contains("HT430BLE")){
                Toast.makeText(this, "找到新裝置"+a.getDevic().getName(), Toast.LENGTH_LONG).show();
                scan.scanLeDevice(false);
                final Intent intent = new Intent(this, Control.class);
                intent.putExtra(EXTRAS_DEVICE_NAME, a.getDevic().getName());
                intent.putExtra(EXTRAS_DEVICE_ADDRESS, a.getDevic().getAddress());
                startActivity(intent);
            }
        }catch (Exception e){}
}

///////////
}
