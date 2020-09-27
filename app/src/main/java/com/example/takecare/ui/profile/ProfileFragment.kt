package com.example.takecare.ui.profile

import android.Manifest
import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.takecare.R
import com.example.takecare.data.repository.UserRepository
import com.example.takecare.utils.PatientUtil
import com.example.takecare.utils.PreferenceHelper
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.android.synthetic.main.fragment_profile.view.*
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Date

class ProfileFragment : Fragment(){

    private lateinit var profileViewModel: ProfileViewModel
    private lateinit var profileBtn: Button
    private lateinit var profileImageView: ImageView
    private lateinit var toolbarImage: ImageView
    private lateinit var profileProgressBar: ProgressBar
    private lateinit var errorText: TextView

    companion object{
        private var IMAGE_PICK_CODE = 1000
        private var PERMISSION_CODE = 1001
        private var imageUri = Uri.parse("")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val root = inflater.inflate(R.layout.fragment_profile, container, false)

        val user = PatientUtil.patient

        profileViewModel = ProfileViewModel(UserRepository())
        setupViewModel()

        profileBtn = root.profile_btn
        profileProgressBar = root.profile_progressBar
        errorText = root.profile_error_text
        profileImageView = root.profile_image

        if (user.birthday.isNullOrBlank()){
            root.profile_birthday.setText("")
        }
        else {
            user.birthday
            val dbFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
            val birthdayDb = LocalDate.parse(user.birthday, dbFormatter)

            val monthText = if(birthdayDb.monthValue < 10) "0${birthdayDb.monthValue}" else birthdayDb.monthValue.toString()
            val dayText = if(birthdayDb.dayOfMonth < 10) "0${birthdayDb.dayOfMonth}" else birthdayDb.dayOfMonth.toString()
            root.profile_birthday.setText("${dayText}-${monthText}-${birthdayDb.year}")
        }

        root.profile_name.setText(if (user.name.isBlank()) "" else user.name)
        root.profile_lastname.setText(if (user.lastName.isNullOrBlank()) "" else user.lastName)
        root.profile_email.setText(if (user.mail.isNullOrBlank()) "" else user.mail)
        root.profile_height.setText(if(user.height == null) "" else user.height.toString())
        root.profile_weight.setText(if(user.weight == null) "" else user.weight.toString())
        root.profile_sex.setSelection(if(user.gender == null) 0 else user.gender!! + 1)

        val localUser = resources.getIdentifier("ic_user_circle_solid", "drawable", this.requireContext().packageName)

        if(user.imageUrl.isNullOrBlank()){
            Glide.with(this.requireContext()).load(localUser).apply(RequestOptions.circleCropTransform())
                .into(profileImageView)
        }else{
            Glide.with(this.requireContext()).load(user.imageUrl).error(localUser)
                .apply(RequestOptions.circleCropTransform())
                .into(profileImageView)
        }

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
            }else if(!mail.contains("@") || !mail.contains(".com")){
                Toast.makeText(this.requireContext(), "Correo inválido, ingrese otro correo", Toast.LENGTH_SHORT).show()
            }else{
                //Format date for database format
                val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
                val formattedDate = LocalDate.parse(birthday, formatter)!!
                val birthdayText = "${formattedDate.year}-${formattedDate.monthValue}-${formattedDate.dayOfMonth}"
                profileViewModel.update(name, lastName, gender, mail, birthdayText, height, weight, imageUri.toString())
            }
        }

        root.profile_change_image.setOnClickListener {
            if(this.requireContext().checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){

                val permissions = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
                requestPermissions(permissions, PERMISSION_CODE)

            }else{
                pickImageFromGallery()
            }
        }

        root.profile_birthday.setOnClickListener {
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
                val monthText = if(mMonth + 1 < 10) "0${mMonth.plus(1)}" else "${mMonth.plus(1)}"
                val dayText = if(mDay < 10) "0${mDay}" else mDay.toString()
                view.setText("" + dayText + "-" + monthText + "-" + mYear)
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

    private fun pickImageFromGallery(){
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, IMAGE_PICK_CODE)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode){
            PERMISSION_CODE -> {
                if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    pickImageFromGallery()
                }
                else{
                    Toast.makeText(this.requireContext(), "Permiso denegado.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == IMAGE_PICK_CODE && resultCode == Activity.RESULT_OK){
            if(data?.data != null){
                imageUri = data.data
                Glide.with(this).load(data.data).apply(RequestOptions.circleCropTransform())
                    .into(profileImageView)
                Toast.makeText(this.requireContext(), "Imagen cargada, pulsa guardar para mantener los cambios", Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(this.requireContext(), "Error al cargar la imagen", Toast.LENGTH_SHORT).show()
            }

        }
    }
}