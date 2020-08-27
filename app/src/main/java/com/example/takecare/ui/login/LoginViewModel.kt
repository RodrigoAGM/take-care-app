package com.example.takecare.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.takecare.data.api.OperationResult
import com.example.takecare.data.api.response.LoginResponse
import com.example.takecare.data.repository.LoginRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


/**
 * Created by Enzo Lizama Paredes on 8/26/20.
 * Contact: lizama.enzo@gmail.com
 */

class LoginViewModel(private val repository: LoginRepository) : ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isLoginSuccess = MutableLiveData<Boolean>()
    val isLoginSuccess: LiveData<Boolean> = _isLoginSuccess

    private val _onMessageError=MutableLiveData<Any>()
    val onMessageError:LiveData<Any> = _onMessageError

    fun login(username: String, password: String) {
        _isLoading.postValue(true)
        viewModelScope.launch {
            val result: OperationResult<LoginResponse> = withContext(Dispatchers.IO) {
                repository.login(username, password)
            }
            _isLoading.postValue(false)
            when (result) {
                is OperationResult.Success -> {
                    _isLoginSuccess.postValue(true)
                }
                is OperationResult.Error -> {
                    _isLoginSuccess.postValue(false)
                    _onMessageError.postValue(result.exception?.message)
                }
            }
        }
    }
}