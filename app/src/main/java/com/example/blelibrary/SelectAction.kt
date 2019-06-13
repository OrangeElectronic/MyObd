package com.example.blelibrary

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.blelibrary.Command.*

class SelectAction : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_action)
    }
    fun onclick(view: View) {
        when(view.id){
            R.id.handshake->{HandShake()
                this.close();}
            R.id.reboot->{Reboot()
            this.close()}
            R.id.Writer->{
                WriteFlash(this,"Sienta2016.S19")
            }
            R.id.waitmode->{
                WriteFlash(this,"TestObd.srec")
            }
        }
    }
    fun close() {
        this.finish()
    }
}
