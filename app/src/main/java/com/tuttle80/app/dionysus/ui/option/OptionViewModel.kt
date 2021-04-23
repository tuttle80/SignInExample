package com.tuttle80.app.dionysus.ui.option

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class OptionViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "Option page"
    }
    val text: LiveData<String> = _text
}