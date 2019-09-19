package com.example.obd.MainActivity

import android.Manifest
import android.annotation.SuppressLint
import android.app.Instrumentation
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.IBinder
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction
import android.support.v4.content.ContextCompat
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.widget.*
import com.airbnb.lottie.LottieAnimationView
import com.orange.obd.R
import com.example.obd.blelibrary.EventBus.*
import com.example.obd.blelibrary.Server.BleServiceControl
import com.example.obd.blelibrary.tool.FormatConvert
import com.example.obd.tool.Command
import com.example.obd.tool.FtpManager
import com.example.obd.tool.LanguageUtil
import com.orango.electronic.orangetxusb.SettingPagr.PrivaryPolicy
import com.orango.electronic.orangetxusb.SettingPagr.Set_Languages
import com.orango.electronic.orangetxusb.mmySql.ItemDAO
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.util.*
import kotlin.concurrent.schedule

class MainPeace : AppCompatActivity(), FragmentManager.OnBackStackChangedListener {

    val itemDAO: ItemDAO by lazy { ItemDAO(applicationContext) }
    override fun onBackStackChanged() {

    }
    companion object {
        var blename:String=""
    }

    var iBinder:IBinder?=null
    var command= Command()
    var SelectMake=""
    var SelectModel=""
    var SelectYear=""
    lateinit var feage:FrameLayout
    lateinit var timer: Timer
    lateinit var textView: TextView
    lateinit var back: ImageView
lateinit var load:RelativeLayout
    private var savedState: Bundle? = null
    var bleServiceControl = BleServiceControl()
    lateinit var anim: LottieAnimationView
    @SuppressLint("ResourceAsColor")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_peace)
        back=findViewById(R.id.imageView13)
        feage=findViewById(R.id.frage)
        EventBus.getDefault().register(this)
        load=findViewById(R.id.load)
        textView=findViewById(R.id.textView11)
        anim=findViewById(R.id.animation_view)
        supportFragmentManager.addOnBackStackChangedListener(this)
        savedState = savedInstanceState
        if(savedState != null) onBackStackChanged()
        timer=Timer()
        timer.schedule(0,3000){
runOnUiThread {
if(!bleServiceControl.isconnect&&!blename.equals("")){
bleServiceControl.connect(blename,this@MainPeace)
    Log.d("情況","連線中")
}
    if(bleServiceControl.isconnect){
        Log.d("情況","connect$blename")
        command.bleServiceControl=bleServiceControl
    }else{     Log.d("情況","tryconnect$blename")}
}
        }
        loading()
        DonloadMMy()
        val profilePreferences = getSharedPreferences("Setting", Context.MODE_PRIVATE)
        val handler = Handler()
        if(profilePreferences.getString("admin","nodata").equals("nodata")){
            Set_Languages.place=0
            PrivaryPolicy.place=0
                feage.setBackgroundColor(resources.getColor(R.color.backgroung))
                val transaction = supportFragmentManager.beginTransaction()
                transaction.replace(R.id.frage, Set_Languages())
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)//設定動畫
                        .commit()
        }else{
                feage.setBackgroundColor(R.color.backgroung)
                val transaction = supportFragmentManager.beginTransaction()
                transaction.replace(R.id.frage, HomeFragement())
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)//設定動畫
                        .commit()
        }
    }
fun DonloadMMy(){
    Thread{
        var a=FtpManager.DownMMy(this)
        if(a){
            handler.post {
                LoadingSuccess()
            }
        }else{
            DonloadMMy()
        }
    }.start()
}
    public override fun onDestroy() {
        timer.cancel()
        if(!bleServiceControl.first){
            unbindService(bleServiceControl.mServiceConnection)
        }
        super.onDestroy()
    }
    fun loadingble(){
        textView.text="Connecting"
        load.visibility= View.VISIBLE
        anim.visibility=View.VISIBLE
    }
    fun loading(){
        textView.text="Data Loading"
load.visibility= View.VISIBLE
        anim.visibility=View.VISIBLE
    }
    fun LoadingSuccess(){
        load.visibility= View.GONE
        anim.visibility=View.GONE
    }
    //----------------------------------------Use bleServiceControl.WriteCmd(HexString,uuid).to write Data ----------------------------------------
    @Subscribe
    fun Event(a: RebackData) {
        try {
            Log.w("WriteReback", "Data:" + FormatConvert.bytesToHex(a.getReback()))
        } catch (e: Exception) {
            Log.w("error", e.message)
        }

    }

    //----------------------------------------Use bleServiceControl.ReadCmd(uuid).to read Data----------------------------------------
    @Subscribe
    fun Event(a: ReadData) {
        try {
            Log.w("ReadReback", "Data:" + FormatConvert.bytesToHex(a.getReback()))
        } catch (e: Exception) {
            Log.w("error", e.message)
        }

    }

    //----------------------------------------Use bleServiceControl.WriteCmd will callbcak this method to confirm is writed susscessfully----------------------------------------
    @Subscribe
    fun Event(a: WriteData) {
        try {
            Log.w("Write", "Data:" + FormatConvert.bytesToHex(a.data()))
        } catch (e: Exception) {
            Log.w("error", e.message)
        }

    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun Event(a: ConnectState) {
        if (a.getReback()) {
            Log.d("連線","連線ok")
        } else {
            handler.post {
                Log.d("連線","Bluetooth is disconnected")
                LoadingSuccess()
                fal=10
                blename=""
                supportFragmentManager.addOnBackStackChangedListener(this)
                val transaction = supportFragmentManager.beginTransaction()
                transaction.add(R.id.frage, HomeFragement(), "Home")
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)//設定動畫
                        // 提交事務
                        .commit()
            }

        }
    }
    var fal=0
    private var handler= Handler()
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun Event(a: ConnectBle) {
        loadingble()
        Thread{
            fal=0
            while(true){
                if(bleServiceControl.isconnect||fal==10){break}
                Thread.sleep(1000)
                fal++
            }
            handler.post {LoadingSuccess()
            if(bleServiceControl.isconnect){
                val transaction = supportFragmentManager!!.beginTransaction()
                transaction.replace(R.id.frage, Selection())
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)//設定動畫
                        .addToBackStack(null)
                        .commit()
            }
            }
        }.start()
    }


    fun onclick(view: View){
        when(view.id){
            R.id.imageView3->{     var intent=Intent(this,Logout::class.java)
                startActivity(intent)}
            R.id.imageView13->{
                goback()
            }
        }

    }
    fun  goback(){
        Thread(){
            try{
                var inst = Instrumentation();
                inst.sendKeyDownUpSync(KeyEvent.KEYCODE_BACK);
            }
            catch ( e:java.lang.Exception) {
                Log.e("Exception when onBack", e.toString());
            }
        }.start();
    }
}
