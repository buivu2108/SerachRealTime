package com.example.searchrealtime

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

open class NumberAdapter : RecyclerView.Adapter<NumberAdapter.ViewHolder>() {

     var numberArray = ArrayList<Int>()
    open fun addData(i:ArrayList<Int>){
        numberArray.addAll(i)
        notifyDataSetChanged()
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_view, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = numberArray[position]
        holder.itemTextView.text = item.toString()
    }

    override fun getItemCount(): Int {
        return numberArray.size
    }

   class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
       var itemTextView: TextView = itemView.findViewById(R.id.itemTextView)
   }
}