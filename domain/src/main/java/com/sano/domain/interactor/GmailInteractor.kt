package com.sano.domain.interactor

import com.sano.domain.repository.GmailRepository
import io.reactivex.Observable

class GmailInteractor(val gmailRepository: GmailRepository) {

    fun getLabels(): Observable<List<String>> {
        return gmailRepository.getLabels()
    }

    val selectedAccountNamePresent: Boolean
        get() = !gmailRepository.selectedAccountName.isNullOrEmpty()


    var selectedAccountName: String?
        set(value) {
            gmailRepository.selectedAccountName = value
        }
        get() {
            return gmailRepository.selectedAccountName
        }
}