package com.smartsettings.ai.core.contextListeners

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.net.wifi.WifiManager
import android.util.Log
import com.smartsettings.ai.core.contextListeners.contextData.WifiContext
import com.smartsettings.ai.core.serializables.SerializableData
import com.smartsettings.ai.di.DependencyProvider


class WifiContextListener(SSID: String) : ContextListener<String>(SerializableData(SSID)) {

    private val context: Context = DependencyProvider.getContext

    private var contextChangeCallback: (() -> Unit)? = null

    private var wifiContext: WifiContext? = null

    private val networkCallback = object : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) {
            super.onAvailable(network)

            val wifiManager = context.getSystemService(Context.WIFI_SERVICE) as WifiManager
            val connectionInfo = wifiManager.connectionInfo
            val ssid = connectionInfo.ssid.replace("\"", "")

            wifiContext = WifiContext(ssid)

            Log.d("XDFCE", "SSID RECV $ssid")

            contextChangeCallback?.invoke()
        }
    }

    override fun startListeningToContextChanges() {
        registerBroadcastReceiver()
    }

    private fun registerBroadcastReceiver() {
        val networkRequestBuilder = NetworkRequest.Builder()
        networkRequestBuilder.addTransportType(NetworkCapabilities.TRANSPORT_WIFI)

        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        connectivityManager.requestNetwork(networkRequestBuilder.build(), networkCallback)
    }

    override fun isListeningPermissionGranted(): Boolean {
        return true
    }

    override fun askListeningPermission(permissionGrantCallback: (Boolean) -> Unit) {
        permissionGrantCallback(true)
    }

    override fun isCriteriaMatches(criteriaData: String): Boolean {
        if (wifiContext?.ssid == criteriaData) {
            return true
        }

        return false
    }

    override fun stopListeningToContextChanges() {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        connectivityManager.unregisterNetworkCallback(networkCallback)
    }
}