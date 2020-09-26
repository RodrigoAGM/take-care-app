package com.example.takecare.ui.advice.quote

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.takecare.R
import com.example.takecare.adapter.QuoteAdapter
import com.example.takecare.adapter.TreatmentAdapter
import com.example.takecare.data.repository.QuoteRepository
import com.example.takecare.model.Quote
import com.example.takecare.ui.advice.treatment.TreatmentViewModel
import kotlinx.android.synthetic.main.activity_quote.*

class QuoteActivity : AppCompatActivity() {

    private lateinit var recyclerQuoteAdapter : QuoteAdapter
    private lateinit var recyclerViewQuote : RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var errorText: TextView
    private lateinit var viewModel: QuoteViewModel
    private lateinit var quoteList: ArrayList<Quote>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quote)

        viewModel = QuoteViewModel(QuoteRepository())
        setupViewModel()
        viewModel.getTreatments()

        progressBar = quote_progressBar
        errorText = quote_error_text

        //Recycler View setup
        quoteList = ArrayList<Quote>()
        recyclerQuoteAdapter = QuoteAdapter(quoteList)
        recyclerViewQuote = recyclerview_quotes
    }

    private fun setupViewModel() {
        viewModel.isLoading.observe(this, isViewLoadingObserver)
        viewModel.isRequestSuccess.observe(this, isRequestSuccess)
        viewModel.onMessageError.observe(this, onMessageError)
        viewModel.quoteData.observe(this, quoteData)
    }

    private val isViewLoadingObserver = Observer<Boolean> {
        val progressBarVisibility = if (it) View.VISIBLE else View.GONE
        val btnVisibility = if (it) View.INVISIBLE else View.VISIBLE
        progressBar.visibility = progressBarVisibility
        recyclerViewQuote.visibility = btnVisibility
    }

    private val isRequestSuccess = Observer<Boolean> {
        if (it) {
            recyclerQuoteAdapter = QuoteAdapter(quoteList)
            recyclerViewQuote.apply {
                adapter = recyclerQuoteAdapter
                layoutManager = LinearLayoutManager(context)
            }
        }
    }

    private val onMessageError = Observer<Any> {
        if(!it.toString().isBlank()){
            progressBar.visibility = View.INVISIBLE
            recyclerViewQuote.visibility = View.INVISIBLE
            errorText.visibility = View.VISIBLE
            errorText.text = it.toString()
        }
    }

    private val quoteData = Observer<List<Quote>> {
        if(!it.isNullOrEmpty()){
            quoteList.addAll(ArrayList(it))
        }
    }
}
