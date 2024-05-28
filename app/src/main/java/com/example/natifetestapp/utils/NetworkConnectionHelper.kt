package com.example.natifetestapp.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch

class NetworkConnectionHelper(
    context: Context,
    dispatcher: CoroutineDispatcher
) : ConnectivityManager.NetworkCallback() {

    private var _isOnline = false
    val isOnline get() = _isOnline

    private val _stateFlow = MutableSharedFlow<Unit>()
    val stateFlow: Flow<Unit> = _stateFlow

    private val scope = CoroutineScope(dispatcher)

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
        scope.launch { _stateFlow.emit(Unit) }
    }

    override fun onUnavailable() {
        super.onUnavailable()
        _isOnline = false
        scope.launch { _stateFlow.emit(Unit) }
    }

    override fun onLost(network: Network) {
        super.onLost(network)
        _isOnline = false
        scope.launch { _stateFlow.emit(Unit) }
    }
}