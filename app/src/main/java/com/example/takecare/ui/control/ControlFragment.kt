package com.example.takecare.ui.control

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.takecare.R
import kotlinx.android.synthetic.main.fragment_control.view.*

class ControlFragment : Fragment() {

    private lateinit var controlViewModel: ControlViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        controlViewModel =
                ViewModelProviders.of(this).get(ControlViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_control, container, false)

        root.control_btn_med.setOnClickListener {
            val builder = AlertDialog.Builder(this.requireContext())
            val mView = layoutInflater.inflate(R.layout.control_dialog, container, false)

            builder.setView(mView)
            val dialog = builder.create()

            val heartRate: EditText = mView.findViewById(R.id.dialog_input_control)
            val btnAccept: Button = mView.findViewById(R.id.dialog_btn_accept)

            btnAccept.setOnClickListener {
                if(heartRate.text.isNullOrBlank()){
                    Toast.makeText(this.requireContext(), "El valor de frecuencia cardiaca no puede ser vacío", Toast.LENGTH_SHORT ).show()
                    dialog.dismiss()
                }else{
                    Toast.makeText(this.requireContext(), "heart rate inserted: ${heartRate.text}", Toast.LENGTH_SHORT ).show()
                    dialog.dismiss()
                }
            }

            dialog.show()
        }

        controlViewModel.text.observe(viewLifecycleOwner, Observer {
        })
        return root
    }
}
