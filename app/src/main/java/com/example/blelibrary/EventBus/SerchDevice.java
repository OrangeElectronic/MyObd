package com.example.blelibrary.EventBus;

import android.bluetooth.BluetoothDevice;

import java.util.ArrayList;

public class SerchDevice {
    private BluetoothDevice mLeDevices;
    public SerchDevice(BluetoothDevice mLeDevices){this.mLeDevices=mLeDevices;}
    public BluetoothDevice getDevic(){
        return mLeDevices;
    }
}
