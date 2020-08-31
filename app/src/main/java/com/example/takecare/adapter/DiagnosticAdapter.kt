package com.example.takecare.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.example.takecare.R
import com.example.takecare.model.Diagnostic
import kotlinx.android.synthetic.main.item_history.view.*
import java.text.SimpleDateFormat

class DiagnosticAdapter(private var diagnostics: ArrayList<Diagnostic>) : RecyclerView.Adapter<DiagnosticAdapter.HistoryViewHolder>(), Filterable {

    private val filteredDiagnostics = ArrayList<Diagnostic>(diagnostics)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        return HistoryViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_history, parent, false)
        )
    }

    override fun getItemCount(): Int = filteredDiagnostics.size

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        val history = filteredDiagnostics[position]
        holder.bind(history)
    }

    inner class HistoryViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title = view.history_card_title
        val frequency = view.history_card_content
        val date = view.history_card_date

        fun bind(diagnostic: Diagnostic) {
            title.text = "NIVEL DE ANSIEDAD: ${diagnostic.level.name}"
            frequency.text = "${diagnostic.frequency.heartRate}\nbpm"
            date.text = "Fecha: ${diagnostic.frequency.date}"

        }
    }

    override fun getFilter(): Filter {
        return DateFilter()
    }

    inner class DateFilter: Filter(){
        override fun performFiltering(text: CharSequence?): FilterResults {

            val filteredList = ArrayList<Diagnostic>()

            if(text.toString().isEmpty()){
                filteredList.addAll(diagnostics)
            }else{
                for(diagnostic in diagnostics){
                    val formatter = SimpleDateFormat("dd/MM/yyyy")
                    val diagnosticDate = formatter.parse(diagnostic.frequency.date)!!

                    val dates = text.toString().split(";")
                    val start = dates[0]
                    val end = dates[1]

                    if(start.isBlank() && !end.isBlank()){
                        val dateEnd = formatter.parse(end)
                        if(diagnosticDate.before(dateEnd) || diagnosticDate == dateEnd){
                            filteredList.add(diagnostic)
                        }
                    }else if(!start.isBlank() && end.isBlank()){
                        val dateStart = formatter.parse(start)
                        if(diagnosticDate.after(dateStart) || diagnosticDate == dateStart){
                            filteredList.add(diagnostic)
                        }
                    }else{
                        val dateStart = formatter.parse(start)
                        val dateEnd = formatter.parse(end)
                        Log.println(Log.ERROR, "Date", dateEnd.toString())
                        Log.println(Log.ERROR, "Date real", diagnosticDate.toString())
                        if((diagnosticDate.before(dateEnd) || diagnosticDate == dateEnd) && (diagnosticDate.after(dateStart)|| diagnosticDate == dateStart)){
                            filteredList.add(diagnostic)
                        }
                    }
                }
            }

            val results = FilterResults()
            results.values = filteredList

            return results
        }

        override fun publishResults(text: CharSequence?, results: FilterResults?) {
            filteredDiagnostics.clear()
            filteredDiagnostics.addAll(results!!.values as Collection<Diagnostic>)
            notifyDataSetChanged()
        }

    }
}