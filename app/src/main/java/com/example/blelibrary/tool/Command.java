package com.example.blelibrary.tool;
import android.content.Context;
import android.os.Handler;
import android.util.Log;

import com.example.blelibrary.Demo.blelibrary.EventBus.Messages;
import com.example.blelibrary.blelibrary.Server.BleServiceControl;
import org.greenrobot.eventbus.EventBus;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import static com.example.blelibrary.Demo.blelibrary.tool.FormatConvert.StringHexToByte;

public  class Command {
    public  static byte[] RXDATA=new byte[0];
    public static String WRITE_SUCCESS="F502000300F40A";
    public static String Program_Flash_Fail="F502000302F60A";
    public static String VERIFY_FAIL="F502000303F70A";
    public BleServiceControl bleServiceControl;
    //自動設定checkbyte
    public  String addcheckbyte(String com){
        byte a[]=StringHexToByte(com);
        byte checkbyte= a[0];
        for(int i=1;i<a.length-2;i++){
            checkbyte ^= a[i];
        }
        a[a.length-2]=checkbyte;
        return bytesToHex(a);
    }
    //握手
    public  boolean HandShake(){
        try{
            RXDATA=new byte[0];;
            String a="0A0000030000F5";
            SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
            Date past=sdf.parse(sdf.format(new Date()));
            bleServiceControl.WriteCmd(addcheckbyte(a),7);
            while(true){
                Date now=sdf.parse(sdf.format(new Date()));
                double time=getDatePoor(now,past);
                if(time>3){return false;}
                if(RXDATA.length==7){return true;}
            }
        }catch (Exception e){e.printStackTrace();return false;}

    }
    //Reboot
    public  boolean Reboot(){
        try{
            RXDATA=new byte[0];;
            String a="0A0D00030000F5";
            bleServiceControl.WriteCmd(addcheckbyte(a),7);
            SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
            Date past=sdf.parse(sdf.format(new Date()));
            while(true){
                Date now=sdf.parse(sdf.format(new Date()));
                double time=getDatePoor(now,past);
                if(time>3){return false;}
                if(RXDATA.equals("F501000300F70A")){return true;}
            }
        }catch (Exception e){e.printStackTrace();return false;}
    }
   private static Handler handler=new Handler() ;
// 燒寫&amp;驗證Flash
    public  boolean WriteFlash(final Context context,final String FileName,final int Ind){
                try{
                    InputStreamReader fr = new InputStreamReader(context.getResources().getAssets().open(FileName));
                    BufferedReader br = new BufferedReader(fr);
                    StringBuilder sb = new StringBuilder();
                    while (br.ready()) {
                        String s=br.readLine();
                        s=s.replace("null","");
                        sb.append(s);
                    }
                    int Long=0;
                    if(sb.length()%Ind == 0){Long=sb.length()/Ind;
                    }else{Long=sb.length()/Ind+1;}
                    Log.d("總行數",""+Long);
                    for(int i=0;i<Long;i++){
int b=i;
if(b>=255){b=b-255;}
                        StringBuffer result = new StringBuffer(Integer.toHexString(b));
                        while (result.length() < 2) {
                            result.insert(0, "0");
                        }
                        String cont=result.toString().toUpperCase();
                        if(i==Long-1){
                            Log.d("行數",""+i);
                            String data=bytesToHex(sb.substring(i*Ind, sb.length()).getBytes());
                            int length=sb.substring(i*Ind, sb.length()).getBytes().length+3;
                            check(Convvvert(data,Integer.toHexString(length),cont));
                            return true;
                        }else{
                            String data=bytesToHex(sb.substring(i*Ind, i*Ind+Ind).getBytes());
                            Log.d("行數",""+i);
                            int length=sb.substring(i*Ind, i*Ind+Ind).getBytes().length+3;
                            if(!check(Convvvert(data,Integer.toHexString(length),cont))){
                                return false;
                            }
                        }
                    }
                    fr.close();
                    return true;
                }catch(Exception e){e.printStackTrace();return false;}
    }
//設定tireid
public  boolean setTireId(final ArrayList<String> Id) {
    ArrayList<String> tmpsend=new ArrayList<>();
    tmpsend.add("60A200FFFFFFFFC20A");

    int i=1;
    for(String id:Id){
        id=AddEmpty(id);
        if(id != null){
            tmpsend.add(addcheckbyte("60A20XidFF0A".replace("id",id).replace("X",""+i)));
        }
        i++;
    }
    tmpsend.add("60A2FFFFFFFFFF3D0A");
    for(String a:tmpsend){
        try{
            bleServiceControl.WriteCmd(a,9);
            Thread.currentThread().sleep(100);
        }catch (Exception e){e.printStackTrace();}
    }
    try{
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
        Date past=sdf.parse(sdf.format(new Date()));
while (true){
    Date now=sdf.parse(sdf.format(new Date()));
    double time=getDatePoor(now,past);
    if(time>15){
        return false;
    }
    if(bytesToHex(RXDATA).equals("60B201FFFFFFFFD30A")){
        return true;
    }
}
    }catch (Exception d){d.printStackTrace();return false;}
}
    public static String AddEmpty(String a){
        switch (a.length()){
            case 6:
                return "00"+a;
            case 7:
                return "0"+a;
            case 8:
                return a;
        }
        return null;
    }
    private static String bytesToHex(byte[] hashInBytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : hashInBytes) {
            sb.append(String.format("%02X", b));
        }
        return sb.toString();
    }
    public static String getDateTime(){

        SimpleDateFormat sdFormat = new SimpleDateFormat("HH:mm:ss:SSS");

        Date date = new Date();

        String strDate = sdFormat.format(date);

//System.out.println(strDate);

        return strDate;

    }
    public  String Convvvert(String data,String length,String line){
        String command="0A02LHX00F5";
        switch(length.length()){
            case 1:
                length="000"+length;
                break;
            case 2:
                length="00"+length;
                break;
            case 3:
                length="0"+length;
                break;
            case 4:
                length=length;
                break;
        }
        if(line.equals("F5")){line="00";}
        if(line.length()>2){line="00";}
        command= addcheckbyte(command.replace("L",length).replace("X", data).replace("H",line));
        return command;
    }
    public  boolean check(String data){
        RXDATA=new byte[0];;
        bleServiceControl.WriteCmd(addcheckbyte(data),8);
        try{
            SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
            Date past=sdf.parse(sdf.format(new Date()));
            int fal=0;
            while(fal<5){
                Date now=sdf.parse(sdf.format(new Date()));
                double time=getDatePoor(now,past);
                if(time>0.3){
                    past=sdf.parse(sdf.format(new Date()));
                    RXDATA=new byte[0];;
                    bleServiceControl.WriteCmd(addcheckbyte(data),8);
                    fal++;
                }
                if(RXDATA.length>0){
                    RXDATA=new byte[0];;
                    return true;
                }
            }
            return false;
        }catch (Exception e){e.printStackTrace();return false;}
    }
    public static void uploaderror(){
        new Thread(new Runnable() {
            @Override
            public void run() {

            }
        }).start();
    }
    public static double getDatePoor(Date endDate, Date nowDate) {
        long diff = endDate.getTime() - nowDate.getTime();
        long sec = diff/1000;
        return sec;
    }
    public  void OneShot(final Context context,final String FileName){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    InputStreamReader fr = new InputStreamReader(context.getResources().getAssets().open(FileName));
                    BufferedReader br = new BufferedReader(fr);
                    StringBuilder sb = new StringBuilder();
                    while (br.ready()) {
                        String s=br.readLine();
                        sb.append(s);
                    }
                    StringBuffer result = new StringBuffer(Integer.toHexString(0));
                    while (result.length() < 2) {
                        result.insert(0, "0");
                    }
                    String cont=result.toString().toUpperCase();
                            String data=bytesToHex(sb.toString().getBytes());
                            int length=sb.toString().getBytes().length+3;
                            if(!check(Convvvert(data,Integer.toHexString(length),cont))){
                                return;
                            }
                    fr.close();
                }catch(Exception e){e.printStackTrace();}
            }
        }).start();
    }
}