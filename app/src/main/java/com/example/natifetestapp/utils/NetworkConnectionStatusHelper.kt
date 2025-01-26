package com.example.natifetestapp.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class NetworkConnectionStatusHelper(context: Context) {

     private val connectivityManager =
        context.getSystemService(ConnectivityManager::class.java) as ConnectivityManager

    private var _isOnline = false
    val isOnline get() = _isOnline

    val networkConnectionStatusFlow: Flow<Boolean> = callbackFlow {
        trySend(false) // To set initial value

        val networkCallback = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                _isOnline = true
                trySend(true)
            }

            override fun onLost(network: Network) {
                _isOnline = false
                trySend(false)
            }
        }

        connectivityManager.registerDefaultNetworkCallback(networkCallback)

        awaitClose {
            connectivityManager.unregisterNetworkCallback(networkCallback)
        }
    }
}