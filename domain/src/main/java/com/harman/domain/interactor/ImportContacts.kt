package com.harman.domain.interactor

import android.net.Uri
import com.harman.domain.repository.ContactsRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject

class ImportContacts constructor(private val contactsRepository: ContactsRepository) :
    UseCase<Uri, Int>() {
    override suspend fun performAction(param: Uri): Int {
        val importContacts = contactsRepository.importContacts(param)
        if (importContacts.isEmpty()) {
            return 0
        }
        val persistedContacts = contactsRepository.getAllContacts()
        persistedContacts.toTypedArray()

        if (persistedContacts.isEmpty()) {
            contactsRepository.insertContacts(importContacts)
            return importContacts.size
        }

        val toInsert = coroutineScope {
            importContacts.map { import ->
                async {
                    if (persistedContacts.find { import == it } == null) import else null
                }
            }
        }.awaitAll().filterNotNull()

        contactsRepository.insertContacts(toInsert)
        return toInsert.size
    }
}