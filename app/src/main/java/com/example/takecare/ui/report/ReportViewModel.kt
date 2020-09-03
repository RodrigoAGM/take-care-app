package com.example.takecare.ui.report

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.takecare.data.api.OperationResult
import com.example.takecare.data.api.response.GetDiagnosticsResponse
import com.example.takecare.data.repository.DiagnosticRepository
import com.example.takecare.model.Diagnostic
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ReportViewModel(private val repository: DiagnosticRepository) : ViewModel() {
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isRequestSuccess = MutableLiveData<Boolean>()
    val isRequestSuccess: LiveData<Boolean> = _isRequestSuccess

    private val _onMessageError = MutableLiveData<Any>()
    val onMessageError:LiveData<Any> = _onMessageError

    private val _diagnosticsData = MutableLiveData<List<Diagnostic>>()
    val diagnosticsData:LiveData<List<Diagnostic>> = _diagnosticsData

    fun getDiagnostics() {
        _isLoading.postValue(true)
        viewModelScope.launch {
            val result: OperationResult<GetDiagnosticsResponse> = withContext(Dispatchers.IO) {
                repository.get()
            }
            _isLoading.postValue(false)
            when (result) {
                is OperationResult.Success -> {
                    _diagnosticsData.postValue(result.data?.diagnostics)
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
