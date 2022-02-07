package com.harman.domain.interactor

import androidx.lifecycle.LiveData
import androidx.paging.PagingData
import com.harman.domain.model.Contact
import com.harman.domain.repository.ContactsRepository

class GetContactsFiltered constructor(private val contactsRepository: ContactsRepository) :
    UseCase<GetContactsFiltered.Param, LiveData<PagingData<Contact>>>() {
    override suspend fun performAction(param: Param) = contactsRepository.getContactsFiltered(param.pageSize, param.filter)

    data class Param(val pageSize: Int, val filter: String)
}