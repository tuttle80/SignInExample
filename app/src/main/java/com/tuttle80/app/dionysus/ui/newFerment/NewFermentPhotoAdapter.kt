package com.tuttle80.app.dionysus.ui.newFerment

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tuttle80.app.dionysus.R


class NewFermentPhotoAdapter(val context: Context, val mList: ArrayList<NewFermentPhotoData>) :
    RecyclerView.Adapter<NewFermentPhotoAdapter.CustomViewHolder>() {

    inner class CustomViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    //    protected var title = view.findViewById<AppCompatTextView>(R.id.title)

        fun bind(simpleData: NewFermentPhotoData) {
           // title.text = simpleData.title
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val layoutView = LayoutInflater.from(context)
            .inflate(R.layout.recycler_item_new_photo, parent, false)

        return CustomViewHolder(layoutView)
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        holder.bind(mList[position])
    }

    override fun getItemCount(): Int {
        return mList.size
    }
}