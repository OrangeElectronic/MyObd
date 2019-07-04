package com.example.blelibrary.Demo

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.blelibrary.Demo.Command.*
import com.example.blelibrary.R
import kotlinx.android.synthetic.main.activity_select_action.*

class SelectAction : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_action)

    }
    fun onclick(view: View) {
        when(view.id){
            R.id.handshake ->{HandShake()
                this.close();}
            R.id.reboot ->{Reboot()
            this.close()}
            R.id.Writer ->{
                WriteFlash(this,"OBD TEST.srec",110)
            }
            R.id.waitmode ->{
                WriteFlash(this,"normal.srec",42)
            }
            R.id.oneflash ->{
                WriteFlash(this,"tucson2.s19",Integer.parseInt(editText.text.toString()))
            }
            R.id.readsensor->{
                WriteFlash(this,"obd123.srec",126)
            }
        }
    }
    fun close() {
        this.finish()
    }
}
