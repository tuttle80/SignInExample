package com.tuttle80.app.dionysus

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager

class Util {
    companion object {
        fun hideKeypad(context: Context, view: View) {
            val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }
}