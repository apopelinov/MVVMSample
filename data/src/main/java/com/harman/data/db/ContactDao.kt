package com.harman.data.db

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.harman.domain.model.Contact

@Dao
abstract class ContactDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun saveContact(contact: Contact): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertContacts(contacts: List<Contact>)

    @Query("SELECT * FROM Contact WHERE id = :id")
    abstract fun getContactById(id: Long): Contact

    @Query("DELETE FROM Contact WHERE id = :id")
    abstract fun deleteContact(id: Long)

    @Query("SELECT * FROM Contact")
    abstract fun getContactsPaging(): PagingSource<Int, Contact>

    @Query(
        """
        SELECT * FROM Contact
        WHERE firstname || ' ' || lastname || phone || email LIKE :filter"""
    )
    abstract fun getContactsFiltered(filter: String): PagingSource<Int, Contact>


    @Query("SELECT * FROM Contact")
    abstract fun getAllContacts(): List<Contact>
}