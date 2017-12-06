package com.sano.domain.repository

import io.reactivex.Observable
import io.reactivex.Single


interface GmailRepository {
    fun getLabels() : Single<List<String>>
    var selectedAccountName: String?
}