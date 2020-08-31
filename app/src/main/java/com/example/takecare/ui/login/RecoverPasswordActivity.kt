package com.example.takecare.ui.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import com.example.takecare.MainActivity
import com.example.takecare.R
import com.example.takecare.data.repository.RecoverPasswordRepository
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_recover_password.*

class RecoverPasswordActivity : AppCompatActivity() {

    private lateinit var viewModel: RecoverPasswordViewModel
    private var mail = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recover_password)

        setSupportActionBar(recover_sub_toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        viewModel = RecoverPasswordViewModel(RecoverPasswordRepository())
        setupViewModel()

        recover_btn.setOnClickListener {
            mail = recover_email.text.trim().toString()
            if(mail.isBlank()){
                Toast.makeText(this, "El mail no puede estar vacío.", Toast.LENGTH_SHORT).show()
            }else{
                viewModel.requestRecoverPassword(mail)
            }
        }
    }

    private fun setupViewModel() {
        viewModel.isLoading.observe(this, isViewLoadingObserver)
        viewModel.isRequestSuccess.observe(this, isRequestSuccess)
        viewModel.onMessageError.observe(this, onMessageError)
    }

    private val isViewLoadingObserver = Observer<Boolean> {
        val progressBarVisibility = if (it) View.VISIBLE else View.GONE
        val btnVisibility = if (it) View.INVISIBLE else View.VISIBLE
        recover_password_progressBar.visibility = progressBarVisibility
        recover_btn.visibility = btnVisibility
    }

    private val isRequestSuccess = Observer<Boolean> {
        if (it) {
            Toast.makeText(this, "Token enviado al correo", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, ChangePasswordActivity::class.java)
            intent.putExtra("mail", mail)
            startActivity(intent)
            finish()
        }
    }

    private val onMessageError = Observer<Any> {
        recover_password_error_text.text = it.toString()
    }
}
