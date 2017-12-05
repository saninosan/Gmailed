package com.sano.gmailed

import android.app.Application
import android.content.Context
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential
import com.google.api.client.util.ExponentialBackOff
import com.google.api.services.gmail.GmailScopes
import com.sano.data.repository.GmailDataRepository
import com.sano.data.repository.RepositoryProvider

private const val PREF_NAME  = "Default"

class App : Application() {
    private val SCOPES = listOf(GmailScopes.GMAIL_LABELS)

    override fun onCreate() {
        super.onCreate()

        RepositoryProvider.gmailRepository = GmailDataRepository(
                GoogleAccountCredential.usingOAuth2(
                        applicationContext, SCOPES)
                        .setBackOff(ExponentialBackOff()),
                getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE))

    }
}