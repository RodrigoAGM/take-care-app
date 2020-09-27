package com.example.takecare.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.takecare.R
import com.example.takecare.model.Quote
import kotlinx.android.synthetic.main.item_quote.view.*
import kotlinx.android.synthetic.main.table_item.view.*
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class QuoteAdapter (private var quotes: ArrayList<Quote>, private var context: Context)  : RecyclerView.Adapter<QuoteAdapter.HistoryViewHolder>() {

    private val filteredQuotes = ArrayList<Quote>(quotes)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        return HistoryViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.table_item, parent, false)
        )
    }

    override fun getItemCount(): Int = filteredQuotes.size

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        val quote = filteredQuotes[position]
        holder.bind(quote)
    }

    inner class HistoryViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val date = view.quote_table_date
        private val time = view.quote_table_time
        private val psychiatrist = view.quote_table_doctor
        private val status = view.quote_table_status

        @SuppressLint("SetTextI18n")
        fun bind(quote: Quote) {

            time.text = "${quote.appointmentTime}"

            val dbFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
            val quoteDate = LocalDate.parse(quote.appointmentDate, dbFormatter)

            val monthText = if(quoteDate.monthValue < 10) "0${quoteDate.monthValue}" else quoteDate.monthValue.toString()
            val dayText = if(quoteDate.dayOfMonth < 10) "0${quoteDate.dayOfMonth}" else quoteDate.dayOfMonth.toString()

            date.text = "${dayText}-${monthText}-${quoteDate.year}"

            psychiatrist.text = "${quote.psychiatristName} ${quote.psychiatristLastName}"
            when(quote.status){
                1->{
                    status.backgroundTintList = context.getColorStateList(R.color.button_default_color)
                    status.text = "Programado"
                }
                2->{
                    status.backgroundTintList = context.getColorStateList(R.color.button_disabled_color)
                    status.text = "Realizado"
                }
                3->{
                    status.backgroundTintList = context.getColorStateList(R.color.button_second_color)
                    status.text = "Cancelado"
                }
            }
        }
    }
}