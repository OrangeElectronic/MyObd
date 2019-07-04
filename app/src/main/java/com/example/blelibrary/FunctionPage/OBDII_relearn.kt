package com.example.blelibrary.FunctionPage


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.blelibrary.MainActivity.MainPeace

import com.example.blelibrary.R

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class OBDII_relearn : Fragment() {
    lateinit var rootView: View
    lateinit var act:MainPeace
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        rootView= inflater.inflate(R.layout.fragment_obdii_relearn, container, false)
        act=activity!!as MainPeace
//        act.loading()
        return rootView
    }


}
