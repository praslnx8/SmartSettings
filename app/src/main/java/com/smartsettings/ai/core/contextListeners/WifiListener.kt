package com.smartsettings.ai.core.contextListeners

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.net.wifi.WifiManager
import android.util.Log
import com.smartsettings.ai.SmartApp
import com.smartsettings.ai.data.contextData.WifiContext
import javax.inject.Inject


class WifiListener : ContextListener() {

    override fun getContextData(): WifiContext? {
        return wifiContext
    }

    @Inject
    lateinit var context: Context

    private var contextChangeCallback: (() -> Unit)? = null

    private var wifiContext: WifiContext? = null

    init {
        SmartApp.appComponent.inject(this)
    }

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

    @SuppressLint("MissingPermission")
    override fun startListeningToContextChanges(contextChangeCallback: () -> Unit) {
        this.contextChangeCallback = contextChangeCallback
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

    override fun stopListeningToContextChanges() {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        connectivityManager.unregisterNetworkCallback(networkCallback)
    }
}