package com.example.shopy.base

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.example.shopy.base.NetworkUtil.getConnectivityStatusString


class NetworkChangeReceiver : BroadcastReceiver() {
companion object{
    var isOnline :Boolean = false
}
    @SuppressLint("LogNotTimber")
    override fun onReceive(context: Context?, intent: Intent) {
        val status = getConnectivityStatusString(context!!)
        Log.d("TAG"," network reciever")
        if ("android.net.conn.CONNECTIVITY_CHANGE" == intent.action) {
            isOnline = status != NetworkUtil.NETWORK_STATUS_NOT_CONNECTED
        }
    }
}