package com.example.obd.tool;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

public class FtpManager {
    public static String ip="35.240.51.141";
    public static String username="orangerd";
    public static String password="orangetpms(~2";
    public static boolean DownMMy( Activity activity) {
        try {
            File DB_PATH = activity.getDatabasePath("mmytb.db");
            File file=new File(DB_PATH.getPath().replace("mmytb.db",""));
            if(!file.exists()){ if(!file.mkdirs()){return false;};}
            return    doloadmmy(DB_PATH.getPath(),activity);

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    public static boolean doloadmmy(String fileanme,Activity activity){
        try{
            SharedPreferences profilePreferences = activity.getSharedPreferences("Setting", Context.MODE_PRIVATE);
            String mmyname=mmyname();
            if(profilePreferences.getString("mmyname","").equals(mmyname)){return true;}
            URL url=new URL("ftp://orangerd:orangetpms@61.221.15.194:21/OrangeTool/Database/MMY/Obd/"+mmyname());
            InputStream is=url.openStream();
            FileOutputStream fos=new FileOutputStream(fileanme);
            int bufferSize = 8192;
            byte[] buf = new byte[bufferSize];
            while(true){
                int read=is.read(buf);
                if(read==-1){  break;}
                fos.write(buf, 0, read);
            }
            is.close();
            fos.close();
            profilePreferences.edit().putString("mmyname",mmyname).commit();

            return true;
        }catch (Exception e){e.printStackTrace(); return false;}


    }
    public static String mmyname(){
        try{
            URL url=new URL("ftp://orangerd:orangetpms@61.221.15.194:21/OrangeTool/Database/MMY/Obd/");
            BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
            String inputLine;
            while ((inputLine = br.readLine()) != null) {
                String[] spi = inputLine.split(" ");
                return spi[spi.length - 1];
            }
            return "nodata";
        }catch (Exception e){e.printStackTrace(); return "nodata";}
    }
    public static boolean DownS19(String Filename,Activity activity){
        return donloads19(Filename,activity);
    }

    public static String GetS19Name(String name){
        try{
            FTPClient ftpClient = new FTPClient();
            ftpClient.connect(ip,21);
            ftpClient.login(username, password);
            FTPFile[] files=ftpClient.listFiles("Drive/OBD DONGLE/"+name);
            if(files.length>0){
                Log.d("filename",files[0].getName());
                return files[0].getName(); }
            return "nodata";
        }catch (Exception e){e.printStackTrace(); return "nodata";}
    }

    public static boolean donloads19(String name,Activity activity){
        try{
            name="DR001";
            URL url=new URL("ftp://"+username+":"+password+"@"+ip+":21/Drive/OBD DONGLE/"+name+"/"+GetS19Name(name));
            InputStream is=url.openStream();
            FileOutputStream fos=new FileOutputStream(activity.getApplicationContext().getFilesDir().getPath()+"/"+name+".srec");
            int bufferSize = 8192;
            byte[] buf = new byte[bufferSize];
            while(true){
                int read=is.read(buf);
                if(read==-1){  break;}
                fos.write(buf, 0, read);
            }
            is.close();
            fos.close();
            return true;
        }catch (Exception e){e.printStackTrace(); return false;}

    }
}
