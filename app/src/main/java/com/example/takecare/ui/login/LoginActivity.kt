package com.example.takecare.ui.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.example.takecare.MainActivity
import com.example.takecare.R
import com.example.takecare.data.repository.LoginRepository
import com.example.takecare.utils.PreferenceHelper
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    private lateinit var viewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {

        /*if (PreferenceHelper.loggedIn){
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }*/

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        viewModel = LoginViewModel(LoginRepository())
        setupViewModel()

        login_btn.setOnClickListener {
            val username = login_username.text.trim().toString()
            val password = login_password.text.trim().toString()

            if(username.isBlank() || password.isBlank()){
                Toast.makeText(this, "El usuario y/o contraseña no pueden estar vacíos.", Toast.LENGTH_SHORT).show()
            }else{
                viewModel.login(username, password)
            }
        }

        login_forgot_password.setOnClickListener {
            val intent = Intent(this, RecoverPasswordActivity::class.java)
            startActivity(intent)
        }

        login_register.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setupViewModel() {
        viewModel.isLoading.observe(this, isViewLoadingObserver)
        viewModel.isLoginSuccess.observe(this, isLoginSuccess)
        viewModel.onMessageError.observe(this, onMessageError)
    }

    private val isViewLoadingObserver = Observer<Boolean> {
        val progressBarVisibility = if (it) View.VISIBLE else View.GONE
        val btnVisibility = if (it) View.INVISIBLE else View.VISIBLE
        login_progressBar.visibility = progressBarVisibility
        login_btn.visibility = btnVisibility
    }

    private val isLoginSuccess = Observer<Boolean> {
        if (it) {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private val onMessageError = Observer<Any> {
        login_error_text.text = it.toString()
    }

}
