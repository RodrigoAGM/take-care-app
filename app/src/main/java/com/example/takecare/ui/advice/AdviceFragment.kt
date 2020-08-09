package com.example.takecare.ui.advice

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.takecare.R


class AdviceFragment : Fragment() {

    private lateinit var adviceViewModel: AdviceViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        adviceViewModel =
            ViewModelProviders.of(this).get(AdviceViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_advice, container, false)
        val textView: TextView = root.findViewById(R.id.text_advice)
        adviceViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })
        return root
    }
}
