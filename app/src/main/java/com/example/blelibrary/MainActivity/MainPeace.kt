package com.example.blelibrary.MainActivity

import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.IBinder
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction
import android.util.Log
import android.view.View
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import com.airbnb.lottie.LottieAnimationView
import com.example.blelibrary.R
import com.example.blelibrary.blelibrary.EventBus.*
import com.example.blelibrary.blelibrary.Server.BleServiceControl
import com.example.blelibrary.blelibrary.tool.FormatConvert
import com.example.blelibrary.tool.Command
import com.example.blelibrary.tool.Command.RXDATA
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.util.*
import kotlin.concurrent.schedule

class MainPeace : AppCompatActivity(), FragmentManager.OnBackStackChangedListener {

    companion object {
        var blename:String=""
    }
    override fun onBackStackChanged() {

    }
    var iBinder:IBinder?=null
    var command= Command()
    lateinit var timer: Timer
    lateinit var textView: TextView
lateinit var load:RelativeLayout
    var bleServiceControl = BleServiceControl()
    lateinit var anim: LottieAnimationView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_peace)
        EventBus.getDefault().register(this)
        load=findViewById(R.id.load)
        textView=findViewById(R.id.textView11)
        anim=findViewById(R.id.animation_view)
        supportFragmentManager.addOnBackStackChangedListener(this)
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.frage, HomeFragement())
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)//設定動畫
                .commit()
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
            Toast.makeText(this,"Bluetooth is disconnected", Toast.LENGTH_SHORT).show()

        }
    }
    private var handler= Handler()
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun Event(a: ConnectBle) {
        loadingble()
        Thread{
            var fal=0
            while(true){
                if(bleServiceControl.isconnect||fal==10){break}
                Thread.sleep(1000)
                fal++
            }
            handler.post {LoadingSuccess()
            if(bleServiceControl.isconnect){
                Toast.makeText(this,"connect fault",Toast.LENGTH_SHORT)
            }else{
                Toast.makeText(this,"connect success",Toast.LENGTH_SHORT)
            }
            }
        }.start()
    }
}
