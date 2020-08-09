package com.example.takecare.ui.history

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.takecare.R
import kotlinx.android.synthetic.main.fragment_history.*
import kotlinx.android.synthetic.main.fragment_history.view.*
import java.util.*

class HistoryFragment : Fragment(){

    private lateinit var historyViewModel: HistoryViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        historyViewModel = ViewModelProviders.of(this).get(HistoryViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_history, container, false)

        val textView: TextView = root.findViewById(R.id.text_history_hello)
        historyViewModel.text.observe(viewLifecycleOwner, Observer {
            //textView.text = it
        })

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
