package com.example.obd.Adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.orange.obd.R
import com.orange_electronic.orangeobd.mmySql.module
import java.util.ArrayList

class FavAdapter(private val name: ArrayList<String>)
    : RecyclerView.Adapter<FavAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.favorite_adapter, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.name.text=name.get(position)
    }

    override fun getItemCount(): Int = name.size

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        val  name:TextView = mView.findViewById(R.id.textView22)
        override fun toString(): String {
            return super.toString() + " ''"
        }
    }
}