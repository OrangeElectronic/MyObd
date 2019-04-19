package com.example.blelibrary;


import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import com.example.blelibrary.EventBus.RebackData;
import com.example.blelibrary.EventBus.SerchDevice;
import com.example.blelibrary.Server.BleServiceControl;
import com.example.blelibrary.Server.ScanDevice;
import com.example.blelibrary.tool.FormatConvert;
import org.greenrobot.eventbus.Subscribe;
import java.util.Timer;
import java.util.TimerTask;


public class MainActivity extends AppCompatActivity {
    ScanDevice scan=new ScanDevice();
    Timer timer;
    BleServiceControl bleServiceControl=new BleServiceControl();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        scan.setmBluetoothAdapter(this);
        timer=new Timer();
        timer.schedule(new tt(),0,2000);
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
            if(a.getDevic().getName().contains("HT430")){
                Toast.makeText(this, "找到新裝置"+a.getDevic().getName(), Toast.LENGTH_LONG).show();
                scan.scanLeDevice(false);
                bleServiceControl.connect(a.getDevic().getAddress(),this);
            }
        }catch (Exception e){Log.w("error",e.getMessage());}
}
    //////////////////////返回數據
  @Subscribe
  public void Event(RebackData a){
      try{
          Log.w("show","返回數據:"+ FormatConvert.ByteToStringbyte(a.getReback()));
      }catch (Exception e){Log.w("error",e.getMessage());}
  }

public class tt extends TimerTask {
    @Override
    public void run() {
        bleServiceControl.WriteCmd("53a901fffffffffb0a","00008d81-0000-1000-8000-00805f9b34fb");
    }
}

}
