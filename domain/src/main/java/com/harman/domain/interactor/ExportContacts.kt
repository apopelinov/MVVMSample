package com.harman.domain.interactor

import android.net.Uri
import com.harman.domain.repository.ContactsRepository

class ExportContacts constructor(private val contactsRepository: ContactsRepository) :
    UseCase<Uri, Unit>() {
    override suspend fun performAction(param: Uri) = contactsRepository.exportContacts(param)
}