package com.harman.data.repository

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.harman.data.db.ContactDao
import com.harman.data.filesystem.FileSystemService
import com.harman.domain.model.Contact
import com.harman.domain.repository.ContactsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class ContactsRepository constructor(
    private val contactDao: ContactDao,
    private val fileSystemService: FileSystemService
) : ContactsRepository {

    override suspend fun getContactsPaging(pageSize: Int): LiveData<PagingData<Contact>> {
        return Pager(
            config = createPagingConfig(pageSize),
            pagingSourceFactory = { contactDao.getContactsPaging() }
        ).liveData
    }

    override suspend fun getContactById(contactId: Long): Contact? {
        return withContext(Dispatchers.IO) {
            contactDao.getContactById(contactId)
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
        ).liveData
    }

    override suspend fun importContacts(uri: Uri): List<Contact> {
        return withContext(Dispatchers.IO) {
            fileSystemService.read(uri, Array<Contact>::class.java).toList()
        }
    }

    override suspend fun exportContacts(uri: Uri) {
        withContext(Dispatchers.IO) {
            val contacts = contactDao.getAllContacts().toTypedArray()
            fileSystemService.write(uri, contacts)
        }
    }

    override suspend fun saveContact(contact: Contact): Long {
        return withContext(Dispatchers.IO) {
            contactDao.saveContact(contact)
        }
    }

    override suspend fun insertContacts(contacts: List<Contact>) {
        withContext(Dispatchers.IO) {
            contactDao.insertContacts(contacts)
        }
    }

    override suspend fun getAllContacts(): List<Contact> {
        return withContext(Dispatchers.IO) {
            contactDao.getAllContacts()
        }
    }

    private fun createPagingConfig(pageSize: Int): PagingConfig {
        return PagingConfig(pageSize = pageSize, enablePlaceholders = false)
    }

    override fun clear() {
    }
}