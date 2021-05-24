package com.tuttle80.app.dionysus.ui.account

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tuttle80.app.dionysus.db.UserDatabase

class SignInViewModel: ViewModel() {

//    private val _count = MutableLiveData<Int>().apply {
//        var userDatabase = UserDatabase.getInstance(requireContext())
//        var accountCount = userDatabase?.userDao()?.getCount() ?: -1;
//
//        value = accountCount.
//    }

    fun getCount(context: Context) : LiveData<Int> {
        var userDatabase = UserDatabase.getInstance(context)
        var zeroValue = MutableLiveData<Int>().apply {
            value = 0
        }
        return userDatabase?.userDao()?.getCount() ?: zeroValue
    }
}