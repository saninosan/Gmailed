package com.sano.gmailed

import android.Manifest
import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.support.annotation.StringRes
import android.text.method.ScrollingMovementMethod
import com.google.api.client.googleapis.extensions.android.gms.auth.GooglePlayServicesAvailabilityIOException
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException
import com.sano.data.repository.RepositoryProvider
import com.sano.domain.interactor.GmailInteractor
import com.sano.gmailed.utils.*
import kotlinx.android.synthetic.main.activity_main.*
import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.EasyPermissions

class MainActivity : Activity(), MainView {
    private lateinit var mProgress: ProgressDialog
    private lateinit var presenter: MainPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        presenter = MainPresenter(this, GmailInteractor(RepositoryProvider.gmailRepository))

        mainText.isVerticalScrollBarEnabled = true
        mainText.movementMethod = ScrollingMovementMethod()

        mProgress = ProgressDialog(this)
        mProgress.setMessage(getString(R.string.progress_description))

        mainButton.setOnClickListener {
            presenter.onCallApiClick()
        }
    }

    @AfterPermissionGranted(REQUEST_PERMISSION_GET_ACCOUNTS)
    override fun chooseAccount() {
        if (EasyPermissions.hasPermissions(
                this, Manifest.permission.GET_ACCOUNTS)) {

            presenter.onChooseAccountPermissionGranted()

        } else {
            EasyPermissions.requestPermissions(
                    this,
                    getString(R.string.permission_rationale),
                    REQUEST_PERMISSION_GET_ACCOUNTS,
                    Manifest.permission.GET_ACCOUNTS)
        }
    }

    override fun onActivityResult(
            requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        presenter.onActivityResult(requestCode, resultCode == Activity.RESULT_OK, data.getExtraStringMap())
    }

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>,
                                            grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    override fun checkGooglePlayServices(): Boolean {
        return isGooglePlayServicesAvailable(this)
    }

    override fun acquireGooglePlayServices() {
        com.sano.gmailed.utils.acquireGooglePlayServices(this, REQUEST_GOOGLE_PLAY_SERVICES)
    }

    override fun isDeviceOnline(): Boolean {
        return com.sano.gmailed.utils.isDeviceOnline(this)
    }

    override fun toggleProgress(isShow: Boolean) {
        if (isShow) {
            mProgress.show()
        } else {
            mProgress.hide()
        }
    }

    override fun setOutputText(text: String) {
        mainText.text = text
    }

    override fun setCallApiButtonEnable(enable: Boolean) {
        mainButton.isEnabled = enable
    }

    override fun onError(error: Throwable) {
        when (error) {
            is GooglePlayServicesAvailabilityIOException ->
                showGooglePlayServicesAvailabilityErrorDialog(MainActivity(), error.connectionStatusCode, REQUEST_GOOGLE_PLAY_SERVICES)
            is UserRecoverableAuthIOException ->
                startActivityForResult(error.intent, REQUEST_AUTHORIZATION)
            else -> mainText.text = getString(R.string.main_error_description, error.message)
        }
    }

    override fun requestAccountPicker(requestCode: Int) {
        startActivityForResult(
                RepositoryProvider.gmailRepository.credential.newChooseAccountIntent(),
                requestCode)
    }

    override fun notify(notification: Notification) {
        when (notification) {
            Notification.NO_NETWORK -> setOutputText(getString(R.string.main_no_network))
            Notification.NO_RESULT -> setOutputText(getString(R.string.main_no_result))
            Notification.REQUIRES_PLAY_SERVICES -> setOutputText(getString(R.string.main_requires_services))
        }
    }
}
