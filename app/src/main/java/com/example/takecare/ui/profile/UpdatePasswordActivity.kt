package com.example.takecare.ui.profile

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import com.example.takecare.R
import com.example.takecare.data.repository.UserRepository
import kotlinx.android.synthetic.main.activity_update_password.*
import java.util.regex.Pattern

class UpdatePasswordActivity : AppCompatActivity() {

    private lateinit var viewModel: UpdatePasswordViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_password)

        viewModel = UpdatePasswordViewModel(UserRepository())
        setupViewModel()

        update_password_confirm_btn.setOnClickListener {

            val password = update_password_new.text.trim().toString()
            val confirmPassword = update_password_confirm_new.text.trim().toString()
            val oldPassword = update_password_old.text.trim().toString()

            val patternLength = Pattern.compile("^(?=\\S+$).{6,}$")
            val patternCapital = Pattern.compile( "^(?=.*[A-Z])(?=\\S+$).{6,}$")
            val patternNumber = Pattern.compile("^(?=.*[0-9])(?=\\S+$).{6,}$")

            if(password.isBlank() || confirmPassword.isBlank() || oldPassword.isBlank()){
                Toast.makeText(this, "Ninguno de los campos puede estar vacío", Toast.LENGTH_SHORT).show()
            }else if (!patternLength.matcher(password).matches()){
                Toast.makeText(this, "Las contraseña debe ser mayor de 6 dígitos", Toast.LENGTH_SHORT).show()
            }else if (!patternCapital.matcher(password).matches()){
                Toast.makeText(this, "Las contraseña debe contener al menos una mayúscula", Toast.LENGTH_SHORT).show()
            }else if (!patternNumber.matcher(password).matches()){
                Toast.makeText(this, "Las contraseña debe contener al menos un número", Toast.LENGTH_SHORT).show()
            }else if(password != confirmPassword){
                Toast.makeText(this, "Las contraseñas no son iguales.", Toast.LENGTH_SHORT).show()
            }else{
                viewModel.updatePassword(password, oldPassword)
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
        update_password_progressBar.visibility = progressBarVisibility
        update_password_confirm_btn.visibility = btnVisibility
    }

    private val isRequestSuccess = Observer<Boolean> {
        if (it) {
            Toast.makeText(this, "Contraseña correctamente actualizada", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    private val onMessageError = Observer<Any> {
        update_password_error_text.text = it.toString()
    }

}
