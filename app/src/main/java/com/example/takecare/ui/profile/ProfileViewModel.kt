package com.example.takecare.ui.profile

import androidx.core.net.toUri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.takecare.data.api.OperationResult
import com.example.takecare.data.api.response.UpdateResponse
import com.example.takecare.data.repository.UserRepository
import com.example.takecare.model.Patient
import com.example.takecare.utils.PatientUtil
import com.example.takecare.utils.PreferenceHelper
import com.google.firebase.storage.FirebaseStorage
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*


class ProfileViewModel(private val repository: UserRepository) : ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isRequestSuccess = MutableLiveData<Boolean>()
    val isRequestSuccess: LiveData<Boolean> = _isRequestSuccess

    private val _onMessageError = MutableLiveData<Any>()
    val onMessageError:LiveData<Any> = _onMessageError

    fun update(name: String, last_name: String, gender: Int?, mail: String,
              birthday: String, height: Double?, weight: Double?, image_url: String?) {
        _isLoading.postValue(true)

        var firebaseImage = ""

        if(!image_url.isNullOrBlank()){
            val storage = FirebaseStorage.getInstance()
            val reference = storage.reference

            val imageRef = reference.child("profile/" + PatientUtil.patient.username)
            imageRef.putFile(image_url.toUri()).addOnSuccessListener {

                imageRef.downloadUrl.addOnSuccessListener {
                    firebaseImage = it.toString()
                }

                viewModelScope.launch {
                    val result: OperationResult<UpdateResponse> = withContext(Dispatchers.IO) {
                        repository.update(name, last_name, gender, mail, birthday, height, weight, firebaseImage)
                    }
                    _isLoading.postValue(false)
                    when (result) {
                        is OperationResult.Success -> {
                            //Save user to Shared Preferences
                            val user = Gson().fromJson(PreferenceHelper.userData, Patient::class.java)
                            user.name = name
                            user.lastName = last_name
                            user.birthday = birthday
                            user.mail = mail
                            user.gender = gender
                            user.height = height
                            user.weight = weight
                            user.imageUrl = firebaseImage
                            PreferenceHelper.userData = Gson().toJson(user)

                            _isRequestSuccess.postValue(true)
                        }
                        is OperationResult.Error -> {
                            _isRequestSuccess.postValue(false)
                            _onMessageError.postValue(result.exception?.message)
                        }
                    }
                }

            }.addOnFailureListener{
                _isRequestSuccess.postValue(false)
                _onMessageError.postValue("Error al subir imagen.")
            }
        }


    }
}