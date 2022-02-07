package com.harman.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import androidx.paging.*
import com.harman.data.db.ContactDao
import com.harman.data.mapper.toDbContact
import com.harman.data.mapper.toDomainContact
import com.harman.domain.model.Contact
import com.harman.domain.repository.ContactsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class ContactsRepository constructor(
    private val contactDao: ContactDao
) : ContactsRepository {

    override suspend fun getContactsPaging(pageSize: Int): LiveData<PagingData<Contact>> {
        return Pager(
            config = createPagingConfig(pageSize),
            pagingSourceFactory = { contactDao.getContactsPaging() }
        ).liveData.map { it.map { dbContact -> dbContact.toDomainContact() } }
    }

    override suspend fun getContactById(contactId: Long): Contact? {
        return withContext(Dispatchers.IO) {
            contactDao.getContactById(contactId)?.toDomainContact()
        }
    }

    override suspend fun deleteContact(contactId: Long) {
        return withContext(Dispatchers.IO) {
            contactDao.deleteContact(contactId)
        }
    }

    override suspend fun getContactsFiltered(
        pageSize: Int,
        filter: String
    ): LiveData<PagingData<Contact>> {
        return Pager(
            config = createPagingConfig(pageSize),
            pagingSourceFactory = { contactDao.getContactsFiltered("%$filter%") }
        ).liveData.map { it.map { dbContact -> dbContact.toDomainContact() } }
    }

    override suspend fun saveContact(contact: Contact): Long {
        return withContext(Dispatchers.IO) {
            contactDao.saveContact(contact.toDbContact())
        }
    }

    override suspend fun insertContacts(contacts: List<Contact>) {
        withContext(Dispatchers.IO) {
            contactDao.insertContacts(contacts.map { it.toDbContact() })
        }
    }

    override suspend fun getAllContacts(): List<Contact> {
        return withContext(Dispatchers.IO) {
            contactDao.getAllContacts().map { it.toDomainContact() }
        }
    }

    private fun createPagingConfig(pageSize: Int): PagingConfig {
        return PagingConfig(pageSize = pageSize, enablePlaceholders = false)
    }

    override fun clear() {
    }
}