package com.example.shopy.base

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

import com.example.shopy.base.NetworkUtil.getConnectivityStatusString


class NetworkChangeReceiver(context: Context?) : BroadcastReceiver() {
    var networkState: Boolean
    init {
        val status = getConnectivityStatusString(context!!)
        networkState = status != NetworkUtil.NETWORK_STATUS_NOT_CONNECTED
        isOnline = networkState
    }

    companion object {
        var isOnline = false
    }

    @SuppressLint("LogNotTimber")
    override fun onReceive(context: Context?, intent: Intent) {
        val status = getConnectivityStatusString(context!!)
        if ("android.net.conn.CONNECTIVITY_CHANGE" == intent.action) {
            isOnline = status != NetworkUtil.NETWORK_STATUS_NOT_CONNECTED
        }
    }
}