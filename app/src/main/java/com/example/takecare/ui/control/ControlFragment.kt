package com.example.takecare.ui.control

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.takecare.R
import com.example.takecare.data.repository.DiagnosticRepository
import com.example.takecare.model.Diagnostic
import kotlinx.android.synthetic.main.fragment_control.view.*
import org.w3c.dom.Text

class ControlFragment : Fragment() {

    private lateinit var viewModel: ControlViewModel
    private lateinit var progressBar: ProgressBar
    private lateinit var errorText: TextView
    private lateinit var dialogButton: Button
    private lateinit var controlContent: TextView
    private lateinit var controlDate: TextView
    private lateinit var controlLevel: TextView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_control, container, false)


        viewModel = ControlViewModel(DiagnosticRepository())
        setupViewModel()

        progressBar = root.control_progressBar
        errorText = root.control_error_text
        dialogButton = root.control_btn_med

        controlContent = root.control_card_content
        controlDate = root.control_card_date
        controlLevel = root.control_card_level

        root.control_btn_med.setOnClickListener {
            val dialogView = layoutInflater.inflate(R.layout.control_dialog, container, false)
            val builder = AlertDialog.Builder(this.requireContext())

            builder.setView(dialogView)
            val dialog = builder.create()

            val heartRate: EditText = dialogView.findViewById(R.id.dialog_input_control)
            val btnAccept: Button = dialogView.findViewById(R.id.dialog_btn_accept)

            btnAccept.setOnClickListener {
                if(heartRate.text.isNullOrBlank()){
                    Toast.makeText(this.requireContext(), "El valor de frecuencia cardiaca no puede ser vacío", Toast.LENGTH_SHORT ).show()
                    dialog.dismiss()
                }else if (heartRate.text.toString().toInt() < 65 ){
                    Toast.makeText(this.requireContext(), "El valor de frecuencia cardiaca no puede ser menor a 65", Toast.LENGTH_SHORT ).show()
                    dialog.dismiss()
                }else if (heartRate.text.toString().toInt() > 230 ){
                    Toast.makeText(this.requireContext(), "Introducir un valor válido.", Toast.LENGTH_LONG ).show()
                    dialog.dismiss()
                }else{
                    viewModel.addDiagnostics(heartRate.text.toString().toInt())
                    dialog.dismiss()
                }
            }
            dialog.show()
        }
        return root
    }

    private fun setupViewModel() {
        viewModel.isLoading.observe(viewLifecycleOwner, isViewLoadingObserver)
        viewModel.isRequestSuccess.observe(viewLifecycleOwner, isRequestSuccess)
        viewModel.onMessageError.observe(viewLifecycleOwner, onMessageError)
        viewModel.diagnosticData.observe(viewLifecycleOwner, diagnosticData)
    }

    private val isViewLoadingObserver = Observer<Boolean> {
        val progressBarVisibility = if (it) View.VISIBLE else View.GONE
        val btnVisibility = if (it) View.INVISIBLE else View.VISIBLE
        progressBar.visibility = progressBarVisibility
        dialogButton.visibility = btnVisibility
    }

    private val isRequestSuccess = Observer<Boolean> {
        if (it) {
            Toast.makeText(this.requireContext(), "Diagnóstico registrado con éxito !", Toast.LENGTH_SHORT).show()
        }
    }

    private val onMessageError = Observer<Any> {
        if(!it.toString().isBlank()){
            progressBar.visibility = View.INVISIBLE
            dialogButton.visibility = View.INVISIBLE
            errorText.visibility = View.VISIBLE
            errorText.text = it.toString()
        }
    }

    private val diagnosticData = Observer<Diagnostic> {
        if(it != null){
            controlContent.text = "${it.frequency.heartRate}\nLPM"
            val date = it.frequency.date.split('/')
            controlDate.text = "Fecha: ${date[2]}-${date[1]}-${date[0]}"
            controlLevel.text = it.level.name
        }
    }
}
