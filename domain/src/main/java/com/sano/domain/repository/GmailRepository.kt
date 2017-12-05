package com.sano.domain.repository

import io.reactivex.Observable


interface GmailRepository {
    fun getLabels() : Observable<List<String>>
    var selectedAccountName: String?
}