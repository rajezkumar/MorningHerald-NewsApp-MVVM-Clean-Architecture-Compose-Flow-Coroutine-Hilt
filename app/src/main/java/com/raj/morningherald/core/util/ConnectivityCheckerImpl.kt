package com.raj.morningherald.core.util

import android.content.Context
import android.net.ConnectivityManager
import javax.inject.Inject

class ConnectivityCheckerImpl @Inject constructor(private val context: Context) :
    ConnectivityChecker {
    override fun hasInternetConnection(): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = cm.activeNetworkInfo
        return activeNetwork?.isConnected ?: false
    }
}