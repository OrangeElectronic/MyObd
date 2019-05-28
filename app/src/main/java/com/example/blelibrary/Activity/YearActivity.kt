package com.example.blelibrary.Activity;

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import com.example.blelibrary.R
import com.orange_electronic.orangeobd.Adapter.YearAdapter
import com.orango.electronic.orangetxusb.mmySql.ItemDAO

class YearActivity : AppCompatActivity() {
lateinit var re:RecyclerView
    lateinit var toolbar: Toolbar
    val itemDAO: ItemDAO by lazy { ItemDAO(applicationContext) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_year)
        toolbar=findViewById(R.id.mmy_toolbar3);
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayShowTitleEnabled(false);
        var make=intent.getStringExtra("make")
        var model=intent.getStringExtra("model")
        re=findViewById(R.id.re)
        re.layoutManager=GridLayoutManager(this,2)
        re.adapter=YearAdapter(itemDAO.getYear(make,model)!!)
    }
}
