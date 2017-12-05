package com.sano.gmailed.utils

import android.app.Activity
import android.content.Context
import android.net.ConnectivityManager

/**
 * Checks whether the device currently has a network connection.
 * @return true if the device has a network connection, false otherwise.
 */
internal fun isDeviceOnline(activity: Activity): Boolean {
    val connMgr = activity.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val networkInfo = connMgr.activeNetworkInfo
    return networkInfo != null && networkInfo.isConnected
}