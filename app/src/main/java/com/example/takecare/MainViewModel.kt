package com.example.takecare

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.takecare.data.api.OperationResult
import com.example.takecare.data.api.response.LoginResponse
import com.example.takecare.data.api.response.LogoutResponse
import com.example.takecare.data.repository.LoginRepository
import com.example.takecare.utils.PreferenceHelper
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainViewModel(private val repository: LoginRepository) : ViewModel() {

    private val _isRequestSuccess = MutableLiveData<Boolean>()
    val isRequestSuccess: LiveData<Boolean> = _isRequestSuccess

    private val _onMessageError=MutableLiveData<Any>()
    val onMessageError:LiveData<Any> = _onMessageError

    fun logout() {

        viewModelScope.launch {
            val result: OperationResult<LogoutResponse> = withContext(Dispatchers.IO) {
                repository.logout()
            }

            when (result) {
                is OperationResult.Success -> {
                    _isRequestSuccess.postValue(true)
                }
                is OperationResult.Error -> {
                    _isRequestSuccess.postValue(false)
                    _onMessageError.postValue(result.exception?.message)
                }
            }
        }
    }
}