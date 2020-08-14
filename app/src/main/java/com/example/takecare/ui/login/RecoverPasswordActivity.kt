package com.example.takecare.ui.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.takecare.R
import kotlinx.android.synthetic.main.activity_recover_password.*

class RecoverPasswordActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recover_password)

        setSupportActionBar(recover_sub_toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        recover_btn.setOnClickListener {
            finish()
        }
    }
}
