package com.sano.gmailed

import android.accounts.AccountManager
import android.text.TextUtils
import com.sano.domain.interactor.GmailInteractor

internal const val REQUEST_ACCOUNT_PICKER = 1000
internal const val REQUEST_AUTHORIZATION = 1001
internal const val REQUEST_GOOGLE_PLAY_SERVICES = 1002
internal const val REQUEST_PERMISSION_GET_ACCOUNTS = 1003

class MainPresenter(var view: MainView, var gmailInteractor: GmailInteractor) {

    fun onCallApiClick() {
        requestLabels()
    }

    fun onActivityResult(requestCode: Int, result: Boolean, data: Map<String, String>?) {
        when (requestCode) {
            REQUEST_GOOGLE_PLAY_SERVICES ->
                if (result) {
                    view.notify(Notification.REQUIRES_PLAY_SERVICES)
                } else {
                    requestLabels()
                }
            REQUEST_ACCOUNT_PICKER ->
                if (result && data != null) {
                    val accountName = data[AccountManager.KEY_ACCOUNT_NAME]
                    if (accountName != null) {
                        gmailInteractor.selectedAccountName = accountName
                        requestLabels()
                    }
                }
            REQUEST_AUTHORIZATION ->
                if (result) {
                    requestLabels()
                }
        }
    }

    fun onChooseAccountPermissionGranted() {
        val accountName = gmailInteractor.selectedAccountName;

        if (accountName != null) {
            requestLabels()
        } else {
            view.requestAccountPicker(REQUEST_ACCOUNT_PICKER)
        }
    }

    private fun requestLabels() {
        when {
            !view.checkGooglePlayServices() -> view.acquireGooglePlayServices()
            !gmailInteractor.selectedAccountNamePresent -> view.chooseAccount()
            !view.isDeviceOnline() -> {
                view.notify(Notification.NO_NETWORK)
            }
            else -> {
                toggleProgress(true)
                view.setOutputText("")

                gmailInteractor.getLabels()
                        .subscribe(
                                { strings ->
                                    if(strings.isEmpty()) {
                                        view.notify(Notification.NO_RESULT)
                                    } else {
                                        view.setOutputText(TextUtils.join("\n", strings))
                                    }
                                    toggleProgress(false)
                                },
                                { error ->
                                    view.onError(error)
                                    toggleProgress(false)
                                })
            }
        }
    }

    private fun toggleProgress(isProgress: Boolean) {
        view.setCallApiButtonEnable(!isProgress)
        view.toggleProgress(isProgress)
    }
}