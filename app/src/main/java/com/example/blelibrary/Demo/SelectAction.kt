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
            R.id.in12->{
                WriteFlash(this,"12in1.s19",392)
            }
            R.id.in9->{
                WriteFlash(this,"9in1.s19",296)
            }
            R.id.Writer ->{
                WriteFlash(this,"6in1.s19",200)
            }
            R.id.waitmode ->{
                WriteFlash(this,"3in1.S19",104)
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
