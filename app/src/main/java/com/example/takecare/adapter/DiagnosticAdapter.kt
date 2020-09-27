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
import java.time.LocalDate
import java.time.format.DateTimeFormatter

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
            title.text = "Nivel de ansiedad - ${diagnostic.level.name}"
            frequency.text = "${diagnostic.frequency.heartRate}\nLPM"

            val dbFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
            val diagnosticDate = LocalDate.parse(diagnostic.frequency.date, dbFormatter)

            val monthText = if(diagnosticDate.monthValue < 10) "0${diagnosticDate.monthValue}" else diagnosticDate.monthValue.toString()
            val dayText = if(diagnosticDate.dayOfMonth < 10) "0${diagnosticDate.dayOfMonth}" else diagnosticDate.dayOfMonth.toString()

            date.text = "Fecha: ${dayText}-${monthText}-${diagnosticDate.year}"

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
                    val dbFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
                    val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
                    val diagnosticDate = LocalDate.parse(diagnostic.frequency.date, dbFormatter)!!

                    val dates = text.toString().split(";")
                    val start = dates[0]
                    val end = dates[1]

                    if(start.isBlank() && !end.isBlank()){
                        val dateEnd = LocalDate.parse(end, formatter)
                        if(diagnosticDate.isBefore(dateEnd) || diagnosticDate == dateEnd){
                            filteredList.add(diagnostic)
                        }
                    }else if(!start.isBlank() && end.isBlank()){
                        val dateStart = LocalDate.parse(start, formatter)
                        if(diagnosticDate.isAfter(dateStart) || diagnosticDate == dateStart){
                            filteredList.add(diagnostic)
                        }
                    }else{
                        val dateStart = LocalDate.parse(start, formatter)
                        val dateEnd = LocalDate.parse(end, formatter)
                        if((diagnosticDate.isBefore(dateEnd) || diagnosticDate == dateEnd) && (diagnosticDate.isAfter(dateStart)|| diagnosticDate == dateStart)){
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