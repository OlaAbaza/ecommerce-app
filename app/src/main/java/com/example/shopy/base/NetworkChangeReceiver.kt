package com.example.shopy.base

import android.annotation.SuppressLint
import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.view.View
import com.example.shopy.R

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

    @SuppressLint("LogNotTimber", "ResourceAsColor")
    override fun onReceive(context: Context?, intent: Intent) {
        val status = getConnectivityStatusString(context!!)
        if ("android.net.conn.CONNECTIVITY_CHANGE" == intent.action) {
            val activity : Activity = context as Activity
            if (status == NetworkUtil.NETWORK_STATUS_NOT_CONNECTED){
                isOnline=false
                activity.findViewById<View>(R.id.viewInternetConnection).visibility=View.VISIBLE
            }else {
                isOnline=true
                activity.findViewById<View>(R.id.viewInternetConnection).visibility=View.GONE
            }

        }
    }
}