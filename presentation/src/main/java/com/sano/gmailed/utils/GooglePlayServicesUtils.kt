package com.sano.gmailed.utils

import android.app.Activity
import android.content.Context
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability

/**
 * Attempt to resolve a missing, out-of-date, invalid or disabled Google
 * Play Services installation via a user dialog, if possible.
 */
internal fun acquireGooglePlayServices(activity: Activity, requestCode: Int) {
    val apiAvailability = GoogleApiAvailability.getInstance()
    val connectionStatusCode = apiAvailability.isGooglePlayServicesAvailable(activity)
    if (apiAvailability.isUserResolvableError(connectionStatusCode)) {
        showGooglePlayServicesAvailabilityErrorDialog(activity, connectionStatusCode, requestCode)
    }
}

/**
 * Display an error dialog showing that Google Play Services is missing
 * or out of date.
 * @param connectionStatusCode code describing the presence (or lack of)
 * Google Play Services on this device.
 */
internal fun showGooglePlayServicesAvailabilityErrorDialog(activity: Activity,
                                                           connectionStatusCode: Int,
                                                           requestCode: Int) {
    val apiAvailability = GoogleApiAvailability.getInstance()
    val dialog = apiAvailability.getErrorDialog(
            activity,
            connectionStatusCode,
            requestCode)
    dialog.show()
}

/**
 * Check that Google Play services APK is installed and up to date.
 * @return true if Google Play Services is available and up to
 * date on this device; false otherwise.
 */
internal fun isGooglePlayServicesAvailable(context: Context): Boolean {
    val apiAvailability = GoogleApiAvailability.getInstance()
    val connectionStatusCode = apiAvailability.isGooglePlayServicesAvailable(context)
    return connectionStatusCode == ConnectionResult.SUCCESS
}