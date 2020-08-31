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
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class HistoryFragment : Fragment(){

    private lateinit var recyclerHistoryAdapter : HistoryAdapter
    private lateinit var dateFrom: EditText
    private lateinit var dateTo: EditText

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val root = inflater.inflate(R.layout.fragment_history, container, false)
        val context = this.requireContext()

        root.text_history_hello.text = root.text_history_hello.text.toString().replace("usuario", PatientUtil.patient.username)

        dateFrom = root.history_date_from
        dateTo = root.history_date_to
        recyclerHistoryAdapter = HistoryAdapter(ArrayList(historyMock))

        root.history_reclycler_view.apply {
            adapter = recyclerHistoryAdapter
            layoutManager = LinearLayoutManager(context)
        }

        root.history_date_picker_from.setOnClickListener {
            pickDate(history_date_from)
        }

        root.history_date_picker_to.setOnClickListener {
            pickDate(history_date_to)
        }

        return root
    }

    private fun pickDate(view:EditText){

        val calendar = Calendar.getInstance()
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val month = calendar.get(Calendar.MONTH)
        val year = calendar.get(Calendar.YEAR)
        val picker = DatePickerDialog(this.requireContext(),
            DatePickerDialog.OnDateSetListener{_, mYear, mMonth, mDay  ->
                view.setText("" + mDay + "/" + mMonth.plus(1) + "/" + mYear)
                filterData()
            }, year, month, day)

        picker.datePicker.maxDate = Date().time
        picker.show()
    }

    private fun filterData(){
        var filterText = if(dateFrom.text.isBlank() && !dateTo.text.isBlank()){
            ";${dateTo.text}"
        }else if(!dateFrom.text.isBlank() && dateTo.text.isBlank()){
            "${dateFrom.text};"
        }else{
            "${dateFrom.text};${dateTo.text}"
        }
        recyclerHistoryAdapter.filter.filter(filterText)
    }
}
