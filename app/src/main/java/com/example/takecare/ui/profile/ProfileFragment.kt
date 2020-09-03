package com.example.takecare.ui.profile

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import com.example.takecare.R
import com.example.takecare.model.Patient
import com.example.takecare.utils.PreferenceHelper
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.android.synthetic.main.fragment_profile.view.*
import java.util.*


class ProfileFragment : Fragment(){

    private lateinit var profileViewModel: ProfileViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val root = inflater.inflate(R.layout.fragment_profile, container, false)

        val user = Gson().fromJson(PreferenceHelper.userData, Patient::class.java)

        root.profile_name.setText(if (user.name.isBlank()) "" else user.name)
        root.profile_lastname.setText(if (user.lastName.isBlank()) "" else user.lastName)
        root.profile_birthday.setText(if (user.birthday.isBlank()) "" else user.birthday)
        root.profile_email.setText(if (user.mail.isBlank()) "" else user.mail)
        root.profile_height.setText(if(user.height == null) "" else user.height.toString())
        root.profile_weight.setText(if(user.weight == null) "" else user.weight.toString())
        root.profile_sex.setSelection(if(user.gender == null) 0 else user.gender + 1)

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
}