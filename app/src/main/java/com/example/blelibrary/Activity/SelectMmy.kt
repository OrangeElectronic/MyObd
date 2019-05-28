package com.example.blelibrary.Activity;
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import com.example.blelibrary.R
import com.orange_electronic.orangeobd.Adapter.makeadapter
import com.orango.electronic.orangetxusb.mmySql.ItemDAO

class SelectMmy : AppCompatActivity() {
    lateinit var re:RecyclerView
    var handler:Handler= Handler()
    lateinit var makeadapter: makeadapter
    lateinit var toolbar: Toolbar
    val itemDAO: ItemDAO by lazy { ItemDAO(applicationContext) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_mmy)
        toolbar = findViewById(R.id.mmy_toolbar)
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayShowTitleEnabled(false);
        re=findViewById(R.id.re)
        re.layoutManager=GridLayoutManager(this,3)
        re.adapter=makeadapter(itemDAO.getMake()!!)
//        Thread(Runnable { var sql=MmyModule.Dowloand("http://61.221.15.194:8080/share.cgi?ssid=0MV6hST&fid=0MV6hST&filename=BT%20OBD%20%E8%BB%8A%E6%AC%BE%E5%88%97%E8%A1%A8for%20APP.csv&openfolder=forcedownload&ep=")
//            var a=sql.size
//            handler.post(Runnable {Log.d("sql",""+a);
//              Log.d("insert",""+itemDAO.insert(sql))
//                val adapter=MmyAdapter(itemDAO.getMake()!!)
//                re.adapter=adapter
//            })
//        }).start()
    }


}
