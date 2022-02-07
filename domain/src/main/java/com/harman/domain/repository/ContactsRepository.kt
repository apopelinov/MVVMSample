package com.harman.domain.repository

import androidx.lifecycle.LiveData
import androidx.paging.PagingData
import com.harman.domain.model.Contact

interface ContactsRepository : Repository {
    suspend fun getContactsPaging(pageSize: Int): LiveData<PagingData<Contact>>
    suspend fun getContactById(contactId: Long): Contact?
    suspend fun deleteContact(contactId: Long)
    suspend fun getContactsFiltered(pageSize: Int, filter: String): LiveData<PagingData<Contact>>
    suspend fun saveContact(contact: Contact): Long
    suspend fun insertContacts(contacts: List<Contact>)
    suspend fun getAllContacts(): List<Contact>
}