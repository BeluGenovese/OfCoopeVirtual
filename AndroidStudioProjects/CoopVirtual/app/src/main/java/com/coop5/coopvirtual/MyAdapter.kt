package com.coop5.coopvirtual

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MyAdapter(private val data: List<MyData>) : RecyclerView.Adapter<MyAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val servicioTextView: TextView = itemView.findViewById(R.id.servicioTextView)
        val periodoTextView: TextView = itemView.findViewById(R.id.periodoTextView)
        val fechaVto1TextView: TextView = itemView.findViewById(R.id.fechaVto1TextView)
        val fechaVto2TextView: TextView = itemView.findViewById(R.id.fechaVto2TextView)
        val fechaVto3TextView: TextView = itemView.findViewById(R.id.fechaVto3TextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_my_data, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = data[position]
        holder.servicioTextView.text = currentItem.DsServicio.trim()
        holder.periodoTextView.text = currentItem.DsPeriodo.trim()
        holder.fechaVto1TextView.text = "Vencimiento 1: " + currentItem.FechaVto1
        holder.fechaVto2TextView.text = "Vencimiento 2: " + currentItem.FechaVto2
        holder.fechaVto3TextView.text = "Vencimiento 3: " + currentItem.FechaVto3
    }

    override fun getItemCount(): Int {
        return data.size
    }
}
