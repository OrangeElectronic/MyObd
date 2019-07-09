package com.example.blelibrary.FunctionPage


import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.v4.app.Fragment
import android.text.InputFilter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import com.example.blelibrary.Demo.Command.HandShake
import com.example.blelibrary.Demo.Command.setTireId
import com.example.blelibrary.Demo.ScanBle
import com.example.blelibrary.Demo.SelectAction
import com.example.blelibrary.MainActivity.HomeFragement
import com.example.blelibrary.MainActivity.MainPeace

import com.example.blelibrary.R
import com.example.blelibrary.tool.CustomTextWatcher
import com.orango.electronic.orangetxusb.mmySql.ItemDAO
import kotlinx.android.synthetic.main.fragment_key__id.view.*
import java.util.ArrayList

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class Key_ID : Fragment() {
    lateinit var rootView: View
    lateinit var act: MainPeace
    companion object {
        val SUCCESS=0
        val FAIL=1
        val WAIT=2
    }
    val itemDAO: ItemDAO by lazy { ItemDAO(activity!!) }
    lateinit var directfit:String
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        rootView=inflater.inflate(R.layout.fragment_key__id, container, false)
        var make=arguments!!.getString(HomeFragement.string_make)
        var model=arguments!!.getString(HomeFragement.string_model)
        var year=arguments!!.getString(HomeFragement.string_year)
         directfit=itemDAO.getPart(make, model, year).directfit
        rootView.mmy_text.text="$make/$model/$year"
        act=activity!!as MainPeace
        rootView.Lft.addTextChangedListener(CustomTextWatcher(rootView.Lft))
        rootView.Rft.addTextChangedListener(CustomTextWatcher(rootView.Rft))
        rootView.Lrt.addTextChangedListener(CustomTextWatcher(rootView.Lrt))
        rootView.Rrt.addTextChangedListener(CustomTextWatcher(rootView.Rrt))
        rootView.Lft.setFilters(arrayOf<InputFilter>(InputFilter.LengthFilter(8)))
        rootView.Rft.setFilters(arrayOf<InputFilter>(InputFilter.LengthFilter(8)))
        rootView.Lrt.setFilters(arrayOf<InputFilter>(InputFilter.LengthFilter(8)))
        rootView.Rrt.setFilters(arrayOf<InputFilter>(InputFilter.LengthFilter(8)))
        rootView.program.setOnClickListener {
            val write = ArrayList<String>()
            if (rootView.Lft.getText().length < 6 || rootView.Lft.getText().length > 8) {
                return@setOnClickListener
            }
            if (rootView.Rft.getText().length < 6 || rootView.Rft.getText().length > 8) {
                return@setOnClickListener
            }
            if (rootView.Lrt.getText().length < 6 || rootView.Lrt.getText().length > 8) {
                return@setOnClickListener
            }
            if (rootView.Rrt.getText().length < 6 || rootView.Rrt.getText().length > 8) {
                return@setOnClickListener
            }
            write.add(rootView.Lft.getText().toString())
            write.add(rootView.Rft.getText().toString())
            write.add(rootView.Lrt.getText().toString())
            write.add(rootView.Rrt.getText().toString())
            act.loading()
            Thread{
                Thread.sleep(10000)
                handler.post {
                    act.LoadingSuccess()
                    updateui(SUCCESS)}
//                var iscuss=false
//                if(!act.command.HandShake()){
//                    act.command.Reboot()
//                }
//                if(act.command.HandShake()){
//                    if( act.command.WriteFlash(act,"s19/$directfit.s19",126)){
//                        if( act.command.setTireId(write)){
//                            iscuss=true
//                        }
//                    }
//                }
//                handler.post {
//                    act.LoadingSuccess()
//                    if(iscuss){     updateui(SUCCESS)}else{
//                        updateui(FAIL)
//                    }
//                }
            }.start()
        }
        updateui(WAIT)
        return rootView
    }
var handler=Handler()
    fun updateui(condition:Int){
        when(condition){
            SUCCESS->{
                rootView.condition.text="Programming complete"
                rootView.condition.setTextColor(resources.getColor(R.color.buttoncolor))
                rootView.Lft.setBackgroundResource(R.mipmap.icon_input_box_ok)
                rootView.Rft.setBackgroundResource(R.mipmap.icon_input_box_ok)
                rootView.Lrt.setBackgroundResource(R.mipmap.icon_input_box_ok)
                rootView.Rrt.setBackgroundResource(R.mipmap.icon_input_box_ok)
                rootView.Lf.setBackgroundResource(R.mipmap.icon_tire_ok)
                rootView.Rf.setBackgroundResource(R.mipmap.icon_tire_ok)
                rootView.Lr.setBackgroundResource(R.mipmap.icon_tire_ok)
                rootView.Rr.setBackgroundResource(R.mipmap.icon_tire_ok)
            }
            FAIL->{
                rootView.condition.text="Program failed"
                rootView.condition.setTextColor(resources.getColor(R.color.colorPrimary))
                rootView.Lft.setBackgroundResource(R.mipmap.icon_input_box_fail)
                rootView.Rft.setBackgroundResource(R.mipmap.icon_input_box_fail)
                rootView.Lrt.setBackgroundResource(R.mipmap.icon_input_box_fail)
                rootView.Rrt.setBackgroundResource(R.mipmap.icon_input_box_fail)
                rootView.Lf.setBackgroundResource(R.mipmap.icon_tire_fail)
                rootView.Rf.setBackgroundResource(R.mipmap.icon_tire_fail)
                rootView.Lr.setBackgroundResource(R.mipmap.icon_tire_fail)
                rootView.Rr.setBackgroundResource(R.mipmap.icon_tire_fail)
            }
            WAIT->{
                rootView.condition.text="Key in the Original sensor ID number"
                rootView.condition.setTextColor(resources.getColor(R.color.buttoncolor))
                rootView.Lft.setBackgroundResource(R.mipmap.icon_input_box_locked)
                rootView.Rft.setBackgroundResource(R.mipmap.icon_input_box_locked)
                rootView.Lrt.setBackgroundResource(R.mipmap.icon_input_box_locked)
                rootView.Rrt.setBackgroundResource(R.mipmap.icon_input_box_locked)
                rootView.Lf.setBackgroundResource(R.mipmap.icon_tire_normal)
                rootView.Rf.setBackgroundResource(R.mipmap.icon_tire_normal)
                rootView.Lr.setBackgroundResource(R.mipmap.icon_tire_normal)
                rootView.Rr.setBackgroundResource(R.mipmap.icon_tire_normal)
            }
        }
    }
}
