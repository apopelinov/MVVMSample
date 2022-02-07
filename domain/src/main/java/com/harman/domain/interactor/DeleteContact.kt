package com.harman.domain.interactor

import com.harman.domain.repository.ContactsRepository

class DeleteContact constructor(private val contactsRepository: ContactsRepository) :
    UseCase<Long, Unit>() {
    override suspend fun performAction(param: Long) = contactsRepository.deleteContact(param)
}