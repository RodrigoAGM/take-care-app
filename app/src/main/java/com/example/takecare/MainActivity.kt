package com.example.takecare

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.PopupMenu
import android.widget.Toast
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.takecare.data.repository.LoginRepository
import com.example.takecare.data.repository.UserRepository
import com.example.takecare.model.Patient
import com.example.takecare.ui.login.LoginActivity
import com.example.takecare.utils.PatientUtil
import com.example.takecare.utils.PreferenceHelper
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_history.view.*

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel : MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel = MainViewModel(LoginRepository())
        setupViewModel()

        main_user_name.text = PatientUtil.patient.username

        toolbar_profile.setOnClickListener {
            val popupMenu = PopupMenu(this, toolbar_profile)
            popupMenu.menuInflater.inflate(R.menu.toolbar_menu, popupMenu.menu)

            popupMenu.setOnMenuItemClickListener {
                MenuItemClick().onMenuItemClick(it)
            }
            popupMenu.show()
        }

        val navView: BottomNavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)
        val appBarConfiguration = AppBarConfiguration(setOf(
            R.id.navigation_profile, R.id.navigation_history, R.id.navigation_control, R.id.navigation_report, R.id.navigation_advice
        ))

        toolbar.setupWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    private inner class MenuItemClick : PopupMenu.OnMenuItemClickListener{
        override fun onMenuItemClick(item: MenuItem?): Boolean {

            if(item!!.title == getString(R.string.menu_logout)){
                viewModel.logout()
            }

            return false
        }

    }

    private fun setupViewModel() {
        viewModel.isRequestSuccess.observe(this, isRequestSuccess)
        viewModel.onMessageError.observe(this, onMessageError)
    }

    private val isRequestSuccess = Observer<Boolean> {
        if (it) {
            PreferenceHelper.loggedIn = false
            PreferenceHelper.userData = ""
            PreferenceHelper.token = ""
            PreferenceHelper.refreshToken = ""

            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private val onMessageError = Observer<Any> {
        if(!it.toString().isBlank()){
            Toast.makeText(this, it.toString(), Toast.LENGTH_SHORT).show()
        }
    }
}