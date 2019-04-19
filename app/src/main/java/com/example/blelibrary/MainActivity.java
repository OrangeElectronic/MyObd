package com.example.blelibrary;


import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.blelibrary.EventBus.ReadData;
import com.example.blelibrary.EventBus.RebackData;
import com.example.blelibrary.EventBus.SerchDevice;
import com.example.blelibrary.EventBus.WriteData;
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
                Toast.makeText(this, "必須打開定位才能啟用此服務", Toast.LENGTH_LONG).show();
            }else{ scan.RequestPermission();}
        }
    }
//----------------------------------------When find Device it will callback on here----------------------------------------
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
//----------------------------------------Use bleServiceControl.WriteCmd(HexString,uuid).to write Data ----------------------------------------
  @Subscribe
  public void Event(RebackData a){
      try{
          Log.w("WriteReback","Data:"+ FormatConvert.ByteToStringbyte(a.getReback()));
      }catch (Exception e){Log.w("error",e.getMessage());}
  }
  //----------------------------------------Use bleServiceControl.ReadCmd(uuid).to read Data----------------------------------------
  @Subscribe
  public void Event(ReadData a){
      try{
          Log.w("ReadReback","Data:"+ FormatConvert.ByteToStringbyte(a.getReback()));
      }catch (Exception e){Log.w("error",e.getMessage());}
  }
  //----------------------------------------Use bleServiceControl.WriteCmd will callbcak this method to confirm is writed susscessfully----------------------------------------
    @Subscribe
    public void Event(WriteData a){
        try{
            Log.w("Write","Data:"+ FormatConvert.ByteToStringbyte(a.data()));
        }catch (Exception e){Log.w("error",e.getMessage());}
    }
public class tt extends TimerTask {
    @Override
    public void run() {
        bleServiceControl.ReadCmd("00002a00-0000-1000-8000-00805f9b34fb");
        bleServiceControl.WriteCmd("53a901fffffffffb0a","00008d81-0000-1000-8000-00805f9b34fb");
    }
}

}
