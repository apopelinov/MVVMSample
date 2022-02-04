package com.harman.domain.interactor

import com.harman.domain.model.Contact
import com.harman.domain.repository.ContactsRepository
import javax.inject.Inject

class GetContactById constructor(private val contactsRepository: ContactsRepository) :
    UseCase<Long, Contact?>() {
    override suspend fun performAction(param: Long) = contactsRepository.getContactById(param)
}