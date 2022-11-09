package com.sgtech.projectc.core

import android.util.Log
import com.sgtech.projectc.core.Constants.TAG

class Utils {
    companion object {
        fun print(e: Exception) {
            Log.e(TAG, e.message ?: e.toString())
        }
    }
}