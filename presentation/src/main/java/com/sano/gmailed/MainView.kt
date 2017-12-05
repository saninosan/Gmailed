package com.sano.gmailed

interface MainView {
    fun checkGooglePlayServices() : Boolean
    fun acquireGooglePlayServices()
    fun isDeviceOnline() : Boolean
    fun chooseAccount()
    fun toggleProgress(isShow: Boolean)
    fun setOutputText(text: String)
    fun setCallApiButtonEnable(enable: Boolean)
    fun onError(error: Throwable)
    fun requestAccountPicker(requestCode: Int)
    fun notify(notification: Notification)
}

enum class Notification {
    REQUIRES_PLAY_SERVICES,
    NO_NETWORK,
    NO_RESULT
}