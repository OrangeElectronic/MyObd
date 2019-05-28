package com.orange_electronic.orangeobd.Adapter


import android.content.Intent
import android.support.v7.widget.RecyclerView

import android.view.LayoutInflater

import android.view.View

import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.example.blelibrary.Activity.YearActivity
import com.example.blelibrary.R
import com.orange_electronic.orangeobd.mmySql.module
import java.util.ArrayList


class ModelAdapter(private val a:ArrayList<module>)
    : RecyclerView.Adapter<ModelAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.select_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.text.text=a[position].modele
        holder.selectbutton.setOnClickListener {
            var intent=Intent(holder.itemView.context, YearActivity::class.java)
            intent.putExtra("make",a[position].make)
            intent.putExtra("model",a[position].modele)
            holder.itemView.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = a.size

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        val text: TextView = mView.findViewById(R.id.text_item)
        val selectbutton:ImageView=mView.findViewById(R.id.select_img)
        override fun toString(): String {
            return super.toString() + " ''"
        }
    }
}