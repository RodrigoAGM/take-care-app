package com.example.takecare.ui.advice.treatment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.takecare.data.api.OperationResult
import com.example.takecare.data.api.response.GetTreatmentResponse
import com.example.takecare.data.repository.TreatmentRepository
import com.example.takecare.model.Treatment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TreatmentViewModel(private val repository: TreatmentRepository) : ViewModel()  {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isRequestSuccess = MutableLiveData<Boolean>()
    val isRequestSuccess: LiveData<Boolean> = _isRequestSuccess

    private val _onMessageError = MutableLiveData<Any>()
    val onMessageError:LiveData<Any> = _onMessageError

    private val _treatmentData = MutableLiveData<List<Treatment>>()
    val treatmentData:LiveData<List<Treatment>> = _treatmentData

    fun getTreatments() {
        _isLoading.postValue(true)
        viewModelScope.launch {
            val result: OperationResult<GetTreatmentResponse> = withContext(Dispatchers.IO) {
                repository.get()
            }
            _isLoading.postValue(false)
            when (result) {
                is OperationResult.Success -> {
                    _treatmentData.postValue(result.data?.treatments)
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