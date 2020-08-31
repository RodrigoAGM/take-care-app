package com.example.takecare.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.example.takecare.R
import com.example.takecare.model.History
import kotlinx.android.synthetic.main.item_history.view.*
import java.text.SimpleDateFormat

class HistoryAdapter(private var histories: ArrayList<History>) : RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder>(), Filterable {

    private val filteredHistories = ArrayList<History>(histories)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        return HistoryViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_history, parent, false)
        )
    }

    override fun getItemCount(): Int = filteredHistories.size

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        val history = filteredHistories[position]
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

    override fun getFilter(): Filter {
        return DateFilter()
    }

    inner class DateFilter: Filter(){
        override fun performFiltering(text: CharSequence?): FilterResults {

            val filteredList = ArrayList<History>()

            if(text.toString().isEmpty()){
                filteredList.addAll(histories)
            }else{
                for(history in histories){
                    val formatter = SimpleDateFormat("dd/MM/yyyy")
                    val historyDate = formatter.parse(history.frequency.date)!!

                    val dates = text.toString().split(";")
                    val start = dates[0]
                    val end = dates[1]

                    if(start.isBlank() && !end.isBlank()){
                        val dateEnd = formatter.parse(end)
                        if(historyDate.before(dateEnd) || historyDate == dateEnd){
                            filteredList.add(history)
                        }
                    }else if(!start.isBlank() && end.isBlank()){
                        val dateStart = formatter.parse(start)
                        if(historyDate.after(dateStart) || historyDate == dateStart){
                            filteredList.add(history)
                        }
                    }else{
                        val dateStart = formatter.parse(start)
                        val dateEnd = formatter.parse(end)
                        Log.println(Log.ERROR, "Date", dateEnd.toString())
                        Log.println(Log.ERROR, "Date real", historyDate.toString())
                        if((historyDate.before(dateEnd) || historyDate == dateEnd) && (historyDate.after(dateStart)|| historyDate == dateStart)){
                            filteredList.add(history)
                        }
                    }
                }
            }

            val results = FilterResults()
            results.values = filteredList

            return results
        }

        override fun publishResults(text: CharSequence?, results: FilterResults?) {
            filteredHistories.clear()
            filteredHistories.addAll(results!!.values as Collection<History>)
            notifyDataSetChanged()
        }

    }
}