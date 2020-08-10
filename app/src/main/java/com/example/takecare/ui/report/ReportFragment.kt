package com.example.takecare.ui.report

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.example.takecare.R
import com.example.takecare.mock.FrequencyMock
import com.example.takecare.model.Frequency
import com.jjoe64.graphview.DefaultLabelFormatter
import com.jjoe64.graphview.series.DataPoint
import com.jjoe64.graphview.series.LineGraphSeries
import kotlinx.android.synthetic.main.fragment_report.view.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class ReportFragment : Fragment() {

    private lateinit var reportViewModel: ReportViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        reportViewModel =
            ViewModelProviders.of(this).get(ReportViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_report, container, false)

        val graph = root.report_graph
        val series = LineGraphSeries<DataPoint>(createDataPoints(FrequencyMock))

        graph.gridLabelRenderer.labelFormatter = LabelFormater()
        graph.addSeries(series)

        reportViewModel.text.observe(viewLifecycleOwner, Observer {
        })
        return root
    }

    private fun createDataPoints(frequencies: List<Frequency>):Array<DataPoint>{
        val dataPointArray = ArrayList<DataPoint>()

        frequencies.forEach {
            val dateFormat = SimpleDateFormat("dd/MM/yyyy")
            val date = dateFormat.parse(it.date)

            dataPointArray.add(DataPoint(date, it.heartRate.toDouble()))
        }

        return dataPointArray.toTypedArray()
    }

    inner class LabelFormater : DefaultLabelFormatter(){
        override fun formatLabel(value: Double, isValueX: Boolean): String {
            val dateFormat = SimpleDateFormat("dd/MM")
            var res = super.formatLabel(value, isValueX)

            if(isValueX){
                res = dateFormat.format(Date(value.toLong()))
            }
            return res
        }
    }
}