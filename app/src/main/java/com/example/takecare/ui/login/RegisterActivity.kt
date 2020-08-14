package com.example.takecare.ui.login

import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import com.example.takecare.MainActivity
import com.example.takecare.R
import kotlinx.android.synthetic.main.activity_register.*
import java.util.*

class RegisterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        setSupportActionBar(register_toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        register_birthday_picker.setOnClickListener {
            PickDate(register_birthday)
        }

        register_btn.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

    fun PickDate(view: EditText){

        val calendar = Calendar.getInstance()
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val month = calendar.get(Calendar.MONTH)
        val year = calendar.get(Calendar.YEAR)

        DatePickerDialog(this,
            DatePickerDialog.OnDateSetListener{ _, mYear, mMonth, mDay  ->
                view.setText("" + mDay + "/" + mMonth.plus(1) + "/" + mYear)
            }, year, month, day).show()
    }
}
