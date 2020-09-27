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
import com.example.takecare.data.repository.QuoteRepository
import com.example.takecare.model.Quote
import kotlinx.android.synthetic.main.activity_quote.*
import kotlinx.android.synthetic.main.activity_treatment.*
import java.time.LocalDate
import java.time.format.DateTimeFormatter

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
        recyclerQuoteAdapter = QuoteAdapter(quoteList, this)
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
        text_past_quote.visibility = btnVisibility
        text_active_quote.visibility = btnVisibility
        table_headers.visibility = btnVisibility
    }

    private val isRequestSuccess = Observer<Boolean> {
        if (it) {

            var activeQuote:Quote? = null

            for (quote in quoteList){
                if(quote.status == 1){
                    activeQuote = quote
                    break
                }
            }

            if(activeQuote != null){
                active_quote.visibility = View.VISIBLE

                active_quote_card_time.text = "${getString(R.string.quote_card_time)} ${activeQuote.appointmentTime}"

                val dbFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
                val quoteDate = LocalDate.parse(activeQuote.appointmentDate, dbFormatter)

                val monthText = if(quoteDate.monthValue < 10) "0${quoteDate.monthValue}" else quoteDate.monthValue.toString()
                val dayText = if(quoteDate.dayOfMonth < 10) "0${quoteDate.dayOfMonth}" else quoteDate.dayOfMonth.toString()

                active_quote_card_date.text = "${getString(R.string.quote_card_date)} ${dayText}-${monthText}-${quoteDate.year}"
                active_quote_card_psychiatrist.text = "${getString(R.string.quote_card_psychiatrist)} ${activeQuote.psychiatristName} ${activeQuote.psychiatristLastName}"

                quoteList.remove(activeQuote)
            }else{
                active_quote_error.text = getString(R.string.quote_no_active)
                active_quote_error.visibility = View.VISIBLE
            }

            if(quoteList.size == 0){
                recyclerViewQuote.visibility = View.INVISIBLE
                errorText.visibility = View.VISIBLE
                errorText.text = getString(R.string.quote_no_past)
            }else{
                recyclerQuoteAdapter = QuoteAdapter(quoteList, this)
                recyclerViewQuote.apply {
                    adapter = recyclerQuoteAdapter
                    layoutManager = LinearLayoutManager(context)
                }
            }
        }
    }

    private val onMessageError = Observer<Any> {
        if(!it.toString().isBlank()){
            progressBar.visibility = View.INVISIBLE
            recyclerViewQuote.visibility = View.INVISIBLE
            active_quote.visibility = View.INVISIBLE
            table_headers.visibility = View.INVISIBLE

            active_quote_error.visibility = View.VISIBLE
            errorText.visibility = View.VISIBLE
            active_quote_error.text = it.toString()
            errorText.text = it.toString()
        }
    }

    private val quoteData = Observer<List<Quote>> {
        if(!it.isNullOrEmpty()){
            quoteList.addAll(ArrayList(it))
        }
    }
}
