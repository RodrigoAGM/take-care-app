package com.example.takecare

import android.app.Activity
import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ImageSpan
import android.util.Log
import android.view.MenuItem
import android.widget.PopupMenu
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.takecare.data.repository.LoginRepository
import com.example.takecare.ui.login.LoginActivity
import com.example.takecare.ui.profile.UpdatePasswordActivity
import com.example.takecare.utils.PatientUtil
import com.example.takecare.utils.PreferenceHelper
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private lateinit var viewModel : MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel = MainViewModel(LoginRepository())
        setupViewModel()

        main_user_name.text = PatientUtil.patient.username
        val localUser = resources.getIdentifier("ic_profile", "drawable", this.packageName)

        if(PatientUtil.patient.imageUrl.isNullOrBlank()){

            Glide.with(this).load(localUser).apply(RequestOptions.circleCropTransform())
                .into(toolbar_profile)
        }else{
            Glide.with(this).load(PatientUtil.patient.imageUrl).error(localUser)
                .apply(RequestOptions.circleCropTransform())
                .into(toolbar_profile)
        }

        toolbar_profile.setOnClickListener {
            val popupMenu = PopupMenu(this, toolbar_profile)
            popupMenu.menuInflater.inflate(R.menu.toolbar_menu, popupMenu.menu)
            popupMenu.menu.add(0, 1, 1, menuIconWithText(getDrawable(R.drawable.ic_lock_black_24dp)!!, getString(R.string.menu_change_pass)))
            popupMenu.menu.add(0, 2, 2, menuIconWithText(getDrawable(R.drawable.ic_logout_black_24dp)!!, getString(R.string.menu_logout)))
            
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

            when(item!!.title.toString().trim()){
                getString(R.string.menu_logout) -> {
                    viewModel.logout()
                }

                getString(R.string.menu_change_pass) -> {
                    val intent = Intent(applicationContext, UpdatePasswordActivity::class.java)
                    startActivity(intent)
                }
            }

            return false
        }
    }

    private fun menuIconWithText(icon: Drawable, title: String): CharSequence? {
        icon.setBounds(0, 0, icon.intrinsicWidth, icon.intrinsicHeight)
        val sb = SpannableString("    $title")
        val imageSpan = ImageSpan(icon, ImageSpan.ALIGN_BOTTOM)
        sb.setSpan(imageSpan, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        return sb
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