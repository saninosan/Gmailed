package com.sano.data.repository

import android.content.SharedPreferences
import com.google.api.client.extensions.android.http.AndroidHttp
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential
import com.google.api.client.json.jackson2.JacksonFactory
import com.google.api.services.gmail.Gmail
import com.sano.domain.repository.GmailRepository
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

private const val PREF_ACCOUNT_NAME = "selectedAccountName"

class GmailDataRepository(initCredential: GoogleAccountCredential, val preference: SharedPreferences) : GmailRepository {


    private var mService: Gmail? = null

    val credential: GoogleAccountCredential = initCredential

    override var selectedAccountName: String?
        set(value) {
            if(value != credential.selectedAccountName) {
                credential.setSelectedAccountName(value)
                setString(PREF_ACCOUNT_NAME, value)
                initService()
            }
        }
        get() {
            if(credential.selectedAccountName.isNullOrEmpty()) {
                credential.selectedAccountName = getString(PREF_ACCOUNT_NAME)
            }
            return credential.selectedAccountName
        }

    init {
        if (selectedAccountName != null) {
            initService()
        }
    }

    private fun initService() {
        val transport = AndroidHttp.newCompatibleTransport()
        val jsonFactory = JacksonFactory.getDefaultInstance()
        mService = com.google.api.services.gmail.Gmail.Builder(
                transport, jsonFactory, credential)
                .setApplicationName("Gmail API Android Quickstart")
                .build()
    }

    override fun getLabels(): Single<List<String>> {
        return Single.fromCallable {
            val user = "me"

            if (mService == null) {
                throw IllegalStateException("Provide credential with valid selected account name")
            }

            val listResponse = mService!!.users().labels().list(user).execute()
            listResponse.labels.map { it.name }
        }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

    }

    private fun setString(key: String, value: String?) {
        val editor = preference.edit()
        editor.putString(key, value)
        editor.apply()
    }

    private fun getString(key: String): String? {
        return preference.getString(key, null)
    }
}