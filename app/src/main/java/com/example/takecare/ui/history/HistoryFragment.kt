package com.example.takecare.ui.history

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.takecare.R
import com.example.takecare.adapter.DiagnosticAdapter
import com.example.takecare.data.repository.DiagnosticRepository
import com.example.takecare.mock.diagnosticsMock
import com.example.takecare.model.Diagnostic
import com.example.takecare.utils.PatientUtil
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.fragment_history.*
import kotlinx.android.synthetic.main.fragment_history.view.*
import java.util.Calendar
import java.util.Date
import kotlin.collections.ArrayList

class HistoryFragment : Fragment(){

    private lateinit var recyclerDiagnosticAdapter : DiagnosticAdapter
    private lateinit var recyclerViewDiagnostic : RecyclerView
    private lateinit var dateFrom: EditText
    private lateinit var dateTo: EditText
    private lateinit var progressBar: ProgressBar
    private lateinit var errorText: TextView
    private lateinit var viewModel: HistoryViewModel
    private lateinit var diagnosticList: ArrayList<Diagnostic>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val root = inflater.inflate(R.layout.fragment_history, container, false)

        //View model setup
        viewModel = HistoryViewModel(DiagnosticRepository())
        setupViewModel()
        viewModel.getDiagnostics()

        //Views setup
        dateFrom = root.history_date_from
        dateTo = root.history_date_to
        progressBar = root.history_progressBar
        errorText = root.history_error_text

        //Recycler View setup
        diagnosticList = ArrayList<Diagnostic>()
        recyclerDiagnosticAdapter = DiagnosticAdapter(diagnosticList)
        recyclerViewDiagnostic = root.history_reclycler_view

        //Change username on screen
        root.text_history_hello.text = root.text_history_hello.text.toString().replace("usuario", PatientUtil.patient.username)

        root.history_date_picker_from.setOnClickListener {
            pickDate(history_date_from)
        }

        root.history_date_picker_to.setOnClickListener {
            pickDate(history_date_to)
        }

        root.clear_filter_btn.setOnClickListener {
            dateFrom.setText("")
            dateTo.setText("")
            recyclerDiagnosticAdapter.filter.filter("")
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
        val filterText = if(dateFrom.text.isBlank() && !dateTo.text.isBlank()){
            ";${dateTo.text}"
        }else if(!dateFrom.text.isBlank() && dateTo.text.isBlank()){
            "${dateFrom.text};"
        }else{
            "${dateFrom.text};${dateTo.text}"
        }
        recyclerDiagnosticAdapter.filter.filter(filterText)
    }

    private fun setupViewModel() {
        viewModel.isLoading.observe(viewLifecycleOwner, isViewLoadingObserver)
        viewModel.isRequestSuccess.observe(viewLifecycleOwner, isRequestSuccess)
        viewModel.onMessageError.observe(viewLifecycleOwner, onMessageError)
        viewModel.diagnosticsData.observe(viewLifecycleOwner, diagnosticsData)
    }

    private val isViewLoadingObserver = Observer<Boolean> {
        val progressBarVisibility = if (it) View.VISIBLE else View.GONE
        val btnVisibility = if (it) View.INVISIBLE else View.VISIBLE
        progressBar.visibility = progressBarVisibility
        recyclerViewDiagnostic.visibility = btnVisibility
    }

    private val isRequestSuccess = Observer<Boolean> {
        if (it) {
            recyclerDiagnosticAdapter = DiagnosticAdapter(diagnosticList)
            recyclerViewDiagnostic.apply {
                adapter = recyclerDiagnosticAdapter
                layoutManager = LinearLayoutManager(context)
            }
        }
    }

    private val onMessageError = Observer<Any> {
        if(!it.toString().isBlank()){
            progressBar.visibility = View.INVISIBLE
            recyclerViewDiagnostic.visibility = View.INVISIBLE
            errorText.visibility = View.VISIBLE
            errorText.text = it.toString()
        }
    }

    private val diagnosticsData = Observer<List<Diagnostic>> {
        if(!it.isNullOrEmpty()){
            diagnosticList.addAll(ArrayList(it))
        }
    }
}
