package com.example.takecare.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.takecare.R
import com.example.takecare.model.Treatment
import kotlinx.android.synthetic.main.item_treatment.view.*
import java.text.SimpleDateFormat
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

            indications.text = treatment.description

            val days = ( treatment.quantity.toDouble() / (24.0/ treatment.frequency.toDouble()) )
            medication.text = "${treatment.quantity} pastillas de ${treatment.medicineName} cada ${treatment.frequency} por ${ceil(days).toInt()} días"

            date.text = "Fecha: ${treatment.creationDate.dropLast(14)}"
            date.isEnabled = (treatment.status == 1)

            psychiatrist.text = "Psiquiatra: ${treatment.psychiatristName} ${treatment.psychiatristLastName}"
        }
    }
}