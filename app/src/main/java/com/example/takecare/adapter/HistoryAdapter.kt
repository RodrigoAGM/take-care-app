package com.example.takecare.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.takecare.R
import com.example.takecare.model.History
import kotlinx.android.synthetic.main.item_history.view.*

class HistoryAdapter(private var histories: List<History>) : RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        return HistoryViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_history, parent, false)
        )
    }

    override fun getItemCount(): Int = histories.size

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        val history = histories[position]
        holder.bind(history)
    }

    inner class HistoryViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title = view.history_card_title
        val frequency = view.history_card_content
        val date = view.history_card_date

        fun bind(history: History) {
            title.text = "NIVEL DE ANSIEDAD: ${history.level.name}"
            frequency.text = "${history.frequency.heartRate}\nbpm"
            date.text = "Fecha: ${history.frequency.date}"

        }
    }
}