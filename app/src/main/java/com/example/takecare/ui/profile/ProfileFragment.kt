package com.example.takecare.ui.profile

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.example.takecare.R
import com.example.takecare.data.repository.UserRepository
import com.example.takecare.model.Patient
import com.example.takecare.utils.PatientUtil
import com.example.takecare.utils.PreferenceHelper
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.android.synthetic.main.fragment_profile.view.*
import java.util.Calendar
import java.util.Date


class ProfileFragment : Fragment(){

    private lateinit var profileViewModel: ProfileViewModel
    private lateinit var profileBtn: Button
    private lateinit var profileProgressBar: ProgressBar
    private lateinit var errorText: TextView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val root = inflater.inflate(R.layout.fragment_profile, container, false)

        val user = Gson().fromJson(PreferenceHelper.userData, Patient::class.java)

        profileViewModel = ProfileViewModel(UserRepository())
        setupViewModel()

        profileBtn = root.profile_btn
        profileProgressBar = root.profile_progressBar
        errorText = root.profile_error_text

        root.profile_name.setText(if (user.name.isBlank()) "" else user.name)
        root.profile_lastname.setText(if (user.lastName.isBlank()) "" else user.lastName)
        root.profile_birthday.setText(if (user.birthday.isBlank()) "" else user.birthday)
        root.profile_email.setText(if (user.mail.isBlank()) "" else user.mail)
        root.profile_height.setText(if(user.height == null) "" else user.height.toString())
        root.profile_weight.setText(if(user.weight == null) "" else user.weight.toString())
        root.profile_sex.setSelection(if(user.gender == null) 0 else user.gender!! + 1)

        profileBtn.setOnClickListener {
            val name = root.profile_name.text.toString()
            val lastName = root.profile_lastname.text.toString()
            val mail = root.profile_email.text.toString()
            val birthday = root.profile_birthday.text.toString()
            val height = if(root.profile_height.text.isNullOrBlank()) 0.0 else root.profile_height.text.toString().toDouble()
            val weight = if(root.profile_weight.text.isNullOrBlank()) 0.0 else root.profile_weight.text.toString().toDouble()
            val gender = root.profile_sex.selectedItemPosition - 1

            if(name.isBlank() || lastName.isBlank() || mail.isBlank() || birthday.isBlank()){
                Toast.makeText(this.requireContext(), "Debes llenar todos los campos obligatorios (*)", Toast.LENGTH_SHORT).show()
            }else{
                profileViewModel.update(name, lastName, gender, mail, birthday, height, weight, "")
            }
        }

        root.profile_birthday_picker.setOnClickListener {
            pickDate(profile_birthday)
        }

        return root
    }

    private fun pickDate(view: EditText){

        val calendar = Calendar.getInstance()
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val month = calendar.get(Calendar.MONTH)
        val year = calendar.get(Calendar.YEAR)

        val picker = DatePickerDialog(this.requireContext(),
            DatePickerDialog.OnDateSetListener{ _, mYear, mMonth, mDay  ->
                view.setText("" + mDay + "/" + mMonth.plus(1) + "/" + mYear)
            }, year, month, day)

        picker.datePicker.maxDate = Date().time
        picker.show()
    }

    private fun setupViewModel() {
        profileViewModel.isLoading.observe(viewLifecycleOwner, isViewLoadingObserver)
        profileViewModel.isRequestSuccess.observe(viewLifecycleOwner, isRequestSuccess)
        profileViewModel.onMessageError.observe(viewLifecycleOwner, onMessageError)
    }

    private val isViewLoadingObserver = Observer<Boolean> {
        val progressBarVisibility = if (it) View.VISIBLE else View.GONE
        val btnVisibility = if (it) View.INVISIBLE else View.VISIBLE
        profileProgressBar.visibility = progressBarVisibility
        profileBtn.visibility = btnVisibility
    }

    private val isRequestSuccess = Observer<Boolean> {
        if (it) {
            PatientUtil.init(PreferenceHelper.userData!!)
            Toast.makeText(this.requireContext(), "Datos actualizados !", Toast.LENGTH_SHORT).show()
        }
    }

    private val onMessageError = Observer<Any> {
        errorText.text = it.toString()
    }
}