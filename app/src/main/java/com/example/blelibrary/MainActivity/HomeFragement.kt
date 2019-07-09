package com.example.blelibrary.MainActivity


import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentTransaction
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.example.blelibrary.R
import kotlinx.android.synthetic.main.fragment_home_fragement.view.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class HomeFragement : Fragment() {
    companion object{
        val string_make="make"
        val string_model="model"
        val string_year="year"
    }
    lateinit var act:MainPeace
    lateinit var rootView: View
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
      rootView= inflater.inflate(R.layout.fragment_home_fragement, container, false)
        act=activity!! as MainPeace
        rootView.selectmmy.setOnClickListener {
//            if(act.bleServiceControl.isconnect){
                val transaction = fragmentManager!!.beginTransaction()
                transaction.replace(R.id.frage, MakeFragement())
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)//設定動畫
                        .addToBackStack(null)
                        .commit()
//            }else{
//                act.startActivity(Intent(act,ScanBle::class.java))
//            }

        }
        return rootView
    }


}
