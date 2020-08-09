package com.example.takecare.ui.advice

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class AdviceViewModel : ViewModel() {
    private val _text = MutableLiveData<String>().apply {
        value = "This is advice Fragment"
    }
    val text: LiveData<String> = _text
}
