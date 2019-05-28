package com.orange_electronic.orangeobd.Adapter


import android.content.Intent
import android.support.v7.widget.RecyclerView

import android.view.LayoutInflater

import android.view.View

import android.view.ViewGroup
import android.widget.ImageView
import com.example.blelibrary.Activity.SelectModel
import com.example.blelibrary.R
import com.orange_electronic.orangeobd.mmySql.module
import java.util.ArrayList


class makeadapter(private val a:ArrayList<module>)
    : RecyclerView.Adapter<makeadapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.make_grid_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
holder.image.setImageResource(holder.itemView.resources.getIdentifier(a[position].image,"mipmap",holder.itemView.context.packageName))
        holder.image.setOnClickListener {
            var intent= Intent(holder.itemView.context, SelectModel::class.java)
            intent.putExtra("make",a[position].make)
holder.itemView.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = a.size

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        val image: ImageView = mView.findViewById(R.id.image)

        override fun toString(): String {
            return super.toString() + " ''"
        }
    }
}