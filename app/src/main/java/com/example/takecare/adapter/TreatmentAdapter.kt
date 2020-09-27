package com.example.takecare.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.takecare.R
import com.example.takecare.model.Treatment
import kotlinx.android.synthetic.main.item_treatment.view.*
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import kotlin.math.ceil

class TreatmentAdapter(private var treatments: ArrayList<Treatment>)  : RecyclerView.Adapter<TreatmentAdapter.HistoryViewHolder>() {

    private val filteredTreatments = ArrayList<Treatment>(treatments)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        return HistoryViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_treatment, parent, false)
        )
    }

    override fun getItemCount(): Int = filteredTreatments.size

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        val treatment = filteredTreatments[position]
        holder.bind(treatment)
    }

    inner class HistoryViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val date = view.treatment_card_date
        private val indications = view.treatment_card_indications
        private val medication = view.treatment_card_medication
        private val psychiatrist = view.treatment_card_psychiatrist

        @SuppressLint("SetTextI18n")
        fun bind(treatment: Treatment) {

            var medicationDescription = ""
            for (medicine in treatment.details){
                val days = ( medicine.quantity.toDouble() / (24.0/ medicine.frequency.toDouble()) )
                medicationDescription += "${medicine.quantity} pastillas de ${medicine.name} cada ${medicine.frequency} horas por ${ceil(days).toInt()} días\n"
            }

            medication.text = medicationDescription
            indications.text = treatment.indications

            val dbFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
            val treatmentDate = LocalDate.parse(treatment.creationDate, dbFormatter)

            val monthText = if(treatmentDate.monthValue < 10) "0${treatmentDate.monthValue}" else treatmentDate.monthValue.toString()
            val dayText = if(treatmentDate.dayOfMonth < 10) "0${treatmentDate.dayOfMonth}" else treatmentDate.dayOfMonth.toString()

            date.text = "Fecha: ${dayText}-${monthText}-${treatmentDate.year}"
            date.isEnabled = (treatment.status == 1)

            psychiatrist.text = "Psiquiatra: ${treatment.psychiatristName} ${treatment.psychiatristLastName}"
        }
    }
}