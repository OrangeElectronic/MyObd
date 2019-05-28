package com.example.blelibrary;

import android.util.Log;

import com.example.blelibrary.blelibrary.Server.BleServiceControl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static com.example.blelibrary.blelibrary.tool.FormatConvert.StringHexToByte;


public class Command {
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
    public static void HandShake(final BleServiceControl bleServiceControl){
        String a="0A0000030000F5";
        bleServiceControl.WriteCmd(addcheckbyte(a));
    }
    //Reboot
    public static void Reboot(final BleServiceControl bleServiceControl){
        String a="0A0D00030000F5";
        bleServiceControl.WriteCmd(addcheckbyte(a));
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
            sb.append(String.format("%02x", b));
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
}
