package com.example.takecare.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.takecare.R
import com.example.takecare.model.Tip
import kotlinx.android.synthetic.main.item_tip.view.*

class InformationAdapter(private var tips: ArrayList<Tip>)  : RecyclerView.Adapter<InformationAdapter.HistoryViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        return HistoryViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_tip, parent, false)
        )
    }

    override fun getItemCount(): Int = tips.size

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        val tip = tips[position]
        holder.bind(tip)
    }

    inner class HistoryViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val title = view.tip_card_title
        private val content = view.tip_card_content
        private val image = view.tip_card_image

        fun bind(tip: Tip) {
            title.text = tip.title
            content.text = tip.content
            image.setImageResource(tip.image)
        }
    }
}