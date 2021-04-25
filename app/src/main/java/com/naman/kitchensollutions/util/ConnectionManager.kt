package com.naman.kitchensollutions.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo

class ConnectionManager {

    fun NetworkAvailabel(context: Context):Boolean{
        val connection=context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activnetwork: NetworkInfo? = connection.activeNetworkInfo
        if (activnetwork?.isConnected!=null){
            return activnetwork.isConnected

        }
        else{
            return false
        }

    }
}