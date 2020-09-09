package com.example.takecare.ui.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import com.example.takecare.R
import com.example.takecare.data.repository.RecoverPasswordRepository
import kotlinx.android.synthetic.main.activity_change_password.*
import kotlinx.android.synthetic.main.activity_recover_password.*
import java.util.regex.Pattern

class ChangePasswordActivity : AppCompatActivity() {

    private lateinit var viewModel: ChangePasswordViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_password)

        val mail = intent.getStringExtra("mail")!!

        viewModel = ChangePasswordViewModel(RecoverPasswordRepository())
        setupViewModel()

        change_confirm_btn.setOnClickListener {
            val password = change_new_password.text.trim().toString()
            val confirmPassword = change_confirm_new_password.text.trim().toString()
            val token = recovery_token.text.trim().toString()

            val patternLength = Pattern.compile("^(?=\\S+$).{6,}$")
            val patternCapital = Pattern.compile( "^(?=.*[A-Z])(?=\\S+$).{6,}$")
            val patternNumber = Pattern.compile("^(?=.*[0-9])(?=\\S+$).{6,}$")

            if(password.isBlank() || confirmPassword.isBlank() || token.isBlank()){
                Toast.makeText(this, "Las contraseñas o el token no pueden ser vacíos", Toast.LENGTH_SHORT).show()
            }else if (patternLength.matcher(password).matches()){
                Toast.makeText(this, "Las contraseña debe ser mayor de 6 dígitos", Toast.LENGTH_SHORT).show()
            }else if (patternCapital.matcher(password).matches()){
                Toast.makeText(this, "Las contraseña debe contener al menos una mayúscula", Toast.LENGTH_SHORT).show()
            }else if (patternNumber.matcher(password).matches()){
                Toast.makeText(this, "Las contraseña debe contener al menos un número", Toast.LENGTH_SHORT).show()
            }else if(password != confirmPassword){
                Toast.makeText(this, "Las contraseñas no son iguales.", Toast.LENGTH_SHORT).show()
            }else{
                viewModel.changePassword(mail, password, token)
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
        change_password_progressBar.visibility = progressBarVisibility
        change_confirm_btn.visibility = btnVisibility
    }

    private val isRequestSuccess = Observer<Boolean> {
        if (it) {
            Toast.makeText(this, "Contraseña correctamente actualizada", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private val onMessageError = Observer<Any> {
        change_password_error_text.text = it.toString()
    }
}
