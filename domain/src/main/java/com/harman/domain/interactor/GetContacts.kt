package com.harman.domain.interactor

import androidx.lifecycle.LiveData
import androidx.paging.PagingData
import com.harman.domain.model.Contact
import com.harman.domain.repository.ContactsRepository

class GetContacts constructor(private val contactsRepository: ContactsRepository) :
    UseCase<Int, LiveData<PagingData<Contact>>>() {
    override suspend fun performAction(param: Int) = contactsRepository.getContactsPaging(param)
}