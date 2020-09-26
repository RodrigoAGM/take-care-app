package com.example.takecare.ui.advice.quote

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.takecare.data.api.OperationResult
import com.example.takecare.data.api.response.GetQuotesResponse
import com.example.takecare.data.repository.QuoteRepository
import com.example.takecare.model.Quote
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class QuoteViewModel(private val repository: QuoteRepository) : ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isRequestSuccess = MutableLiveData<Boolean>()
    val isRequestSuccess: LiveData<Boolean> = _isRequestSuccess

    private val _onMessageError = MutableLiveData<Any>()
    val onMessageError:LiveData<Any> = _onMessageError

    private val _quoteData = MutableLiveData<List<Quote>>()
    val quoteData:LiveData<List<Quote>> = _quoteData

    fun getTreatments() {
        _isLoading.postValue(true)
        viewModelScope.launch {
            val result: OperationResult<GetQuotesResponse> = withContext(Dispatchers.IO) {
                repository.get()
            }
            _isLoading.postValue(false)
            when (result) {
                is OperationResult.Success -> {
                    _quoteData.postValue(result.data?.quotes)
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