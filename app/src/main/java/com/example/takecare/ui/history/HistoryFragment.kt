package com.example.takecare.ui.history

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.takecare.R
import com.example.takecare.adapter.HistoryAdapter
import com.example.takecare.mock.historyMock
import com.example.takecare.model.Patient
import com.example.takecare.utils.PatientUtil
import com.example.takecare.utils.PreferenceHelper
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_history.*
import kotlinx.android.synthetic.main.fragment_history.view.*
import java.util.*

class HistoryFragment : Fragment(){

    private lateinit var recyclerHistory : RecyclerView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val root = inflater.inflate(R.layout.fragment_history, container, false)
        val context = this.requireContext()

        root.text_history_hello.text = root.text_history_hello.text.toString().replace("usuario", PatientUtil.patient.username)

        root.history_reclycler_view.apply {
            adapter = HistoryAdapter(
                historyMock
            )
            layoutManager = LinearLayoutManager(context)
        }

        root.history_date_picker_from.setOnClickListener {
            PickDate(history_date_from)
        }

        root.history_date_picker_to.setOnClickListener {
            PickDate(history_date_to)
        }

        return root
    }

    fun PickDate(view:EditText){

        val calendar = Calendar.getInstance()
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val month = calendar.get(Calendar.MONTH)
        val year = calendar.get(Calendar.YEAR)

        DatePickerDialog(this.requireContext(),
            DatePickerDialog.OnDateSetListener{_, mYear, mMonth, mDay  ->
                view.setText("" + mDay + "/" + mMonth.plus(1) + "/" + mYear)
            }, year, month, day).show()
    }
}
