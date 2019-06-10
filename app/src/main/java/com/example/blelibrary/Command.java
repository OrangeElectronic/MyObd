package com.example.blelibrary;
import android.content.Context;
import android.os.Handler;
import android.util.Log;
import com.example.blelibrary.blelibrary.Server.BleServiceControl;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static com.example.blelibrary.MainActivity.bleServiceControl;
import static com.example.blelibrary.blelibrary.tool.FormatConvert.StringHexToByte;
public class Command {
    public static String RX="nodatas";
    public static String WRITE_SUCCESS="F502000300F40A";
    public static String Program_Flash_Fail="F502000300F40A";
    public static String VERIFY_FAIL="F502000300F40A";
    //自動設定checkbyte
    public static String addcheckbyte(String com){
        byte a[]=StringHexToByte(com);
        byte checkbyte= a[0];
        for(int i=1;i<a.length-2;i++){
            checkbyte ^= a[i];
        }
        a[a.length-2]=checkbyte;
        return bytesToHex(a);
    }
    //握手
    public static void HandShake(){
        String a="0A0000030000F5";
        bleServiceControl.WriteCmd(addcheckbyte(a));
    }
    //Reboot
    public static void Reboot(){
        String a="0A0D00030000F5";
        bleServiceControl.WriteCmd(addcheckbyte(a));
    }
   private static Handler handler=new Handler() ;
// 燒寫&amp;驗證Flash
    public static void WriteFlash(final Context context, final BleServiceControl bleServiceControl){
       final int Longer=126;
       bleServiceControl.WriteCmd(addcheckbyte("0A0200803A45323E3136313E3645334534313C3B303F3B3F30303B3F39413A3E38333E34464437353E46343E3446313E363E463437354439413A31343C444541303B3F3B303F3B3F30303B3F3E3136413A3934313C3142363B3F30303B3F3B303F3B3F30313E3641393A313C344231363B303F3B3F30303B3F3B303F3B3F30453A3CAAF5"));
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    InputStreamReader fr = new InputStreamReader(context.getResources().getAssets().open("Sienta2016.S19"));
                    BufferedReader br = new BufferedReader(fr);
                    StringBuilder sb = new StringBuilder();
                    while (br.ready()) {
                        String s=br.readLine();
                        sb.append(s);
                    }
                    int Long=0;
                    if(sb.length()%126 == 0){Long=sb.length()/126;
                    }else{Long=sb.length()/126+1;}
                    for(int i=0;i<Long;i++){
                        if(i==Long-1){
                            String data=bytesToHex(sb.substring(i*126, sb.length()).getBytes());
                            int length=sb.substring(i*126, sb.length()).getBytes().length+2;
                            System.out.println(Convvvert(data,Integer.toHexString(length)));
                        }else{
                            String data=bytesToHex(sb.substring(i*126, i*126+126).getBytes());
                            int length=sb.substring(i*126, i*126+126).getBytes().length+2;
                            System.out.println(Convvvert(data,Integer.toHexString(length)));
                        }
                    }
                    fr.close();
                }catch(Exception e){e.printStackTrace();}
            }
        }).start();

    }
//設定tireid
public static void setTireId(final ArrayList<String> Id, final BleServiceControl bleServiceControl) {
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
    bleServiceControl.WriteArray(tmpsend);
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

        SimpleDateFormat sdFormat = new SimpleDateFormat("hh:mm:ss");

        Date date = new Date();

        String strDate = sdFormat.format(date);

//System.out.println(strDate);

        return strDate;

    }
    public static String Convvvert(String data,String length){
        String command="0A02LX00F5";
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
        command= addcheckbyte(command.replace("L",length).replace("X", data));
        return command;
    }
}
