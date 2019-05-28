package com.example.blelibrary.Activity;

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import com.example.blelibrary.R
import com.orange_electronic.orangeobd.Adapter.ModelAdapter
import com.orango.electronic.orangetxusb.mmySql.ItemDAO

class SelectModel : AppCompatActivity() {
    val itemDAO: ItemDAO by lazy { ItemDAO(applicationContext) }
    lateinit var  re:RecyclerView
    lateinit var toolbar:Toolbar
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_model)
        toolbar=findViewById(R.id.mmy_toolbar2);
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayShowTitleEnabled(false);
        re=findViewById(R.id.re)
        re.layoutManager= GridLayoutManager(this,2)
        re.adapter= ModelAdapter(itemDAO.getmodel(intent.getStringExtra("make"))!!)
        setTitle("選擇車型")

    }
}
