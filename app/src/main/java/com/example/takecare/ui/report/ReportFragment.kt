package com.example.takecare.ui.report

import android.app.DatePickerDialog
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.takecare.R
import com.example.takecare.adapter.DiagnosticAdapter
import com.example.takecare.data.repository.DiagnosticRepository
import com.example.takecare.mock.FrequencyMock
import com.example.takecare.model.Diagnostic
import com.example.takecare.model.Frequency
import com.example.takecare.ui.history.HistoryViewModel
import com.jjoe64.graphview.DefaultLabelFormatter
import com.jjoe64.graphview.GraphView
import com.jjoe64.graphview.series.DataPoint
import com.jjoe64.graphview.series.LineGraphSeries
import kotlinx.android.synthetic.main.fragment_history.view.*
import kotlinx.android.synthetic.main.fragment_report.*
import kotlinx.android.synthetic.main.fragment_report.view.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class ReportFragment : Fragment() {

    private lateinit var viewModel: ReportViewModel
    private lateinit var reportGraph : GraphView
    private lateinit var progressBar: ProgressBar
    private lateinit var errorText: TextView
    private lateinit var dateFrom: EditText
    private lateinit var dateTo: EditText
    private lateinit var diagnosticList: ArrayList<Diagnostic>
    private lateinit var filteredDiagnosticList: ArrayList<Diagnostic>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val root = inflater.inflate(R.layout.fragment_report, container, false)
        reportGraph = root.report_graph

        //View model setup
        viewModel = ReportViewModel(DiagnosticRepository())
        setupViewModel()
        viewModel.getDiagnostics()

        //Views setup
        dateFrom = root.report_date_from
        dateTo = root.report_date_to
        progressBar = root.report_progressBar
        errorText = root.report_error_text

        diagnosticList = ArrayList<Diagnostic>()
        filteredDiagnosticList = ArrayList<Diagnostic>()

        root.report_date_picker_from.setOnClickListener {
            pickDate(report_date_from)
        }

        root.report_date_picker_to.setOnClickListener {
            pickDate(report_date_to)
        }
        return root
    }

    private fun createDataPoints(frequencies: ArrayList<Diagnostic>):Array<DataPoint>{
        val dataPointArray = ArrayList<DataPoint>()
        var num = 0.0

        if(frequencies.size == 1){
            dataPointArray.add(DataPoint(0.0, frequencies[0].frequency.heartRate.toDouble()))
            dataPointArray.add(DataPoint(0.0, frequencies[0].frequency.heartRate.toDouble()))
        }else{
            frequencies.forEach {
                dataPointArray.add(DataPoint(num, it.frequency.heartRate.toDouble()))
                num += 1
            }
        }

        return dataPointArray.toTypedArray()
    }

    private fun pickDate(view: EditText){

        val calendar = Calendar.getInstance()
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val month = calendar.get(Calendar.MONTH)
        val year = calendar.get(Calendar.YEAR)

        DatePickerDialog(this.requireContext(),
            DatePickerDialog.OnDateSetListener{ _, mYear, mMonth, mDay  ->
                view.setText("" + mDay + "/" + mMonth.plus(1) + "/" + mYear)
                filterData()
            }, year, month, day).show()
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
        reportGraph.visibility = btnVisibility
    }

    private val isRequestSuccess = Observer<Boolean> {
        if (it) {
            val series = LineGraphSeries<DataPoint>(createDataPoints(filteredDiagnosticList))

            reportGraph.viewport.isXAxisBoundsManual = true
            reportGraph.viewport.setMaxX(filteredDiagnosticList.size.toDouble()-1)
            reportGraph.gridLabelRenderer.horizontalAxisTitle = "Diagnósticos"
            reportGraph.gridLabelRenderer.isHorizontalLabelsVisible = false
            reportGraph.addSeries(series)
            series.isDrawDataPoints = true
            series.dataPointsRadius = 15f
        }
    }

    private val onMessageError = Observer<Any> {
        if(!it.toString().isBlank()){
            progressBar.visibility = View.INVISIBLE
            reportGraph.visibility = View.INVISIBLE
            errorText.visibility = View.VISIBLE
            errorText.text = it.toString()
        }
    }

    private val diagnosticsData = Observer<List<Diagnostic>> {
        if(!it.isNullOrEmpty()){
            diagnosticList.addAll(ArrayList(it))
            filteredDiagnosticList.addAll(ArrayList(it))
        }
    }

    private fun filterData(){
        val formatter = SimpleDateFormat("dd/MM/yyyy")
        val newFilteredList = ArrayList<Diagnostic>()
        val end = dateTo.text.toString()
        val start = dateFrom.text.toString()

        for(diagnostic in diagnosticList){
            val diagnosticDate = formatter.parse(diagnostic.date)!!

            if(start.isBlank() && !end.isBlank()){
                val dateEnd = formatter.parse(end)
                if(diagnosticDate.before(dateEnd) || diagnosticDate == dateEnd){
                    newFilteredList.add(diagnostic)
                }
            }else if(!start.isBlank() && end.isBlank()){
                val dateStart = formatter.parse(start)
                if(diagnosticDate.after(dateStart) || diagnosticDate == dateStart){
                    newFilteredList.add(diagnostic)
                }
            }else{
                val dateStart = formatter.parse(start)
                val dateEnd = formatter.parse(end)
                if((diagnosticDate.before(dateEnd) || diagnosticDate == dateEnd) && (diagnosticDate.after(dateStart)|| diagnosticDate == dateStart)){
                    newFilteredList.add(diagnostic)
                }
            }
        }
        filteredDiagnosticList.clear()
        filteredDiagnosticList.addAll(newFilteredList)

        val series = LineGraphSeries<DataPoint>(createDataPoints(filteredDiagnosticList))

        reportGraph.removeAllSeries()
        reportGraph.viewport.isXAxisBoundsManual = true
        reportGraph.viewport.setMaxX(filteredDiagnosticList.size.toDouble()-1)
        reportGraph.gridLabelRenderer.horizontalAxisTitle = "Diagnósticos"
        reportGraph.gridLabelRenderer.isHorizontalLabelsVisible = false
        reportGraph.addSeries(series)
        series.isDrawDataPoints = true
        series.dataPointsRadius = 15f
    }
}