package com.example.natifetestapp.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest

class NetworkConnectionHelper(context: Context) : ConnectivityManager.NetworkCallback() {

    private var _isOnline = false
    val isOnline get() = _isOnline

    init {
        val connectivityManager =
            context.getSystemService(ConnectivityManager::class.java) as ConnectivityManager

        val networkRequest = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
            .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
            .build()

        connectivityManager.requestNetwork(networkRequest, this)
    }



    override fun onAvailable(network: Network) {
        super.onAvailable(network)
        _isOnline = true
    }

    override fun onUnavailable() {
        super.onUnavailable()
        _isOnline = false
    }

    override fun onLost(network: Network) {
        super.onLost(network)
        _isOnline = false
    }
}