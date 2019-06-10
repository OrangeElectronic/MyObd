package com.example.blelibrary;

import android.bluetooth.BluetoothDevice;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.blelibrary.Adapter.SelectBle;
import com.example.blelibrary.blelibrary.EventBus.SerchDevice;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

import static com.example.blelibrary.MainActivity.bleServiceControl;
import static com.example.blelibrary.MainActivity.scan;

public class ScanBle extends AppCompatActivity {
    ArrayList<BluetoothDevice> ble=new ArrayList<>();
    SelectBle selectBle=new SelectBle(ble,this);
    RecyclerView re;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_ble);
        re=findViewById(R.id.re);
        re.setLayoutManager(new LinearLayoutManager(this));
        re.setAdapter(selectBle);
        EventBus.getDefault().register(this);
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void Event(SerchDevice a){
        try{
            if(!ble.contains(a.getDevic())){  ble.add(a.getDevic());
                selectBle.notifyDataSetChanged();}
        }catch (Exception e){
            Log.w("error",e.getMessage());}
    }
    public void close(View view){
        scan.scanLeDevice(false);
        this.finish();
    }
}
