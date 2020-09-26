package com.example.takecare.ui.advice.treatment

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.takecare.R
import com.example.takecare.adapter.TreatmentAdapter
import com.example.takecare.data.repository.TreatmentRepository
import com.example.takecare.model.Treatment
import com.example.takecare.ui.history.HistoryViewModel
import kotlinx.android.synthetic.main.activity_treatment.*

class TreatmentActivity : AppCompatActivity() {

    private lateinit var recyclerTreatmentAdapter : TreatmentAdapter
    private lateinit var recyclerViewTreatment : RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var errorText: TextView
    private lateinit var viewModel: TreatmentViewModel
    private lateinit var treatmentList: ArrayList<Treatment>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_treatment)

        viewModel = TreatmentViewModel(TreatmentRepository())
        setupViewModel()
        viewModel.getTreatments()

        progressBar = treatment_progressBar
        errorText = treatment_error_text

        //Recycler View setup
        treatmentList = ArrayList<Treatment>()
        recyclerTreatmentAdapter = TreatmentAdapter(treatmentList)
        recyclerViewTreatment = recyclerview_treatment
    }

    private fun setupViewModel() {
        viewModel.isLoading.observe(this, isViewLoadingObserver)
        viewModel.isRequestSuccess.observe(this, isRequestSuccess)
        viewModel.onMessageError.observe(this, onMessageError)
        viewModel.treatmentData.observe(this, treatmentData)
    }

    private val isViewLoadingObserver = Observer<Boolean> {
        val progressBarVisibility = if (it) View.VISIBLE else View.GONE
        val btnVisibility = if (it) View.INVISIBLE else View.VISIBLE
        progressBar.visibility = progressBarVisibility
        recyclerViewTreatment.visibility = btnVisibility
    }

    private val isRequestSuccess = Observer<Boolean> {
        if (it) {
            recyclerTreatmentAdapter = TreatmentAdapter(treatmentList)
            recyclerViewTreatment.apply {
                adapter = recyclerTreatmentAdapter
                layoutManager = LinearLayoutManager(context)
            }
        }
    }

    private val onMessageError = Observer<Any> {
        if(!it.toString().isBlank()){
            progressBar.visibility = View.INVISIBLE
            recyclerViewTreatment.visibility = View.INVISIBLE
            errorText.visibility = View.VISIBLE
            errorText.text = it.toString()
        }
    }

    private val treatmentData = Observer<List<Treatment>> {
        if(!it.isNullOrEmpty()){
            treatmentList.addAll(ArrayList(it))
        }
    }
}
