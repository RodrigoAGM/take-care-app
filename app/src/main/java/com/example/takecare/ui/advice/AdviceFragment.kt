package com.example.takecare.ui.advice

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.takecare.R
import kotlinx.android.synthetic.main.fragment_advice.*
import kotlinx.android.synthetic.main.fragment_advice.view.*


class AdviceFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val root = inflater.inflate(R.layout.fragment_advice, container, false)

        root.advice_btn_breath_methods.setOnClickListener {
            val intent = Intent(this.requireContext(), BreathActivity::class.java)
            startActivity(intent)
        }

        root.advice_btn_relax_sounds.setOnClickListener {
            val intent = Intent(this.requireContext(), SoundsActivity::class.java)
            startActivity(intent)
        }

        root.advice_btn_information.setOnClickListener {
            val intent = Intent(this.requireContext(), InformationActivity::class.java)
            startActivity(intent)
        }

        return root
    }
}
