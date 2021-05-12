package com.tuttle80.app.dionysus.ui.home

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.tuttle80.app.dionysus.R


class HomeListAdapter(val context: Context, val mList: ArrayList<HomeListSimpleData>) :
    RecyclerView.Adapter<HomeListAdapter.CustomViewHolder>() {

    inner class CustomViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        protected var title = view.findViewById<AppCompatTextView>(R.id.title)

        fun bind(simpleData: HomeListSimpleData) {
            title.text = simpleData.title
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val layoutView = LayoutInflater.from(context)
            .inflate(R.layout.home_list_item, parent, false)

        return CustomViewHolder(layoutView)
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        holder.bind(mList[position])
    }

    override fun getItemCount(): Int {
        return mList.size
    }
}