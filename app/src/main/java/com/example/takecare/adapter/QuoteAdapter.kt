package com.example.takecare.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.takecare.R
import com.example.takecare.model.Quote
import kotlinx.android.synthetic.main.item_quote.view.*

class QuoteAdapter (private var quotes: ArrayList<Quote>)  : RecyclerView.Adapter<QuoteAdapter.HistoryViewHolder>() {

    private val filteredQuotes = ArrayList<Quote>(quotes)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        return HistoryViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_quote, parent, false)
        )
    }

    override fun getItemCount(): Int = filteredQuotes.size

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        val quote = filteredQuotes[position]
        holder.bind(quote)
    }

    inner class HistoryViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val date = view.quote_card_date
        private val time = view.quote_card_time
        private val psychiatrist = view.quote_card_psychiatrist

        @SuppressLint("SetTextI18n")
        fun bind(quote: Quote) {

            time.text = "Hora de la cita: ${quote.appointmentTime}"
            date.text = "Fecha: ${quote.appointmentDate.dropLast(14)}"
            psychiatrist.text = "Psiquiatra: ${quote.psychiatristName} ${quote.psychiatristLastName}"
        }
    }
}