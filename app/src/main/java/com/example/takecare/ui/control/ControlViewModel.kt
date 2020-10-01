package com.example.takecare.ui.control

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.takecare.data.api.OperationResult
import com.example.takecare.data.api.response.AddDiagnosticResponse
import com.example.takecare.data.api.response.GetDiagnosticsResponse
import com.example.takecare.data.repository.DiagnosticRepository
import com.example.takecare.model.Diagnostic
import com.example.takecare.model.Frequency
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

class ControlViewModel(private val repository: DiagnosticRepository) : ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isRequestSuccess = MutableLiveData<Boolean>()
    val isRequestSuccess: LiveData<Boolean> = _isRequestSuccess

    private val _onMessageError = MutableLiveData<Any>()
    val onMessageError:LiveData<Any> = _onMessageError

    private val _diagnosticData = MutableLiveData<Diagnostic>()
    val diagnosticData:LiveData<Diagnostic> = _diagnosticData

    fun addDiagnostics(heartRate: Int) {
        _isLoading.postValue(true)
        viewModelScope.launch {

            val calendar = Calendar.getInstance()
            val day = calendar.get(Calendar.DAY_OF_MONTH)
            val month = calendar.get(Calendar.MONTH)
            val year = calendar.get(Calendar.YEAR)

            val date = "${year}/${month+1}/${day}"
            val frequency = Frequency(heartRate, date)

            val result: OperationResult<AddDiagnosticResponse> = withContext(Dispatchers.IO) {
                repository.add(frequency, date, "New Diagnostic")
            }
            _isLoading.postValue(false)
            when (result) {
                is OperationResult.Success -> {
                    _diagnosticData.postValue(result.data?.diagnostic)
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