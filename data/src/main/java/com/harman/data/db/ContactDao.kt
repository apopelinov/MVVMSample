package com.harman.data.db

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.harman.data.db.model.DbContact

@Dao
abstract class ContactDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun saveContact(contact: DbContact): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertContacts(contacts: List<DbContact>)

    @Query("SELECT * FROM DbContact WHERE id = :id")
    abstract fun getContactById(id: Long): DbContact?

    @Query("DELETE FROM DbContact WHERE id = :id")
    abstract fun deleteContact(id: Long)

    @Query("SELECT * FROM DbContact")
    abstract fun getContactsPaging(): PagingSource<Int, DbContact>

    @Query(
        """
        SELECT * FROM DbContact
        WHERE firstname || ' ' || lastname || phone || email LIKE :filter"""
    )
    abstract fun getContactsFiltered(filter: String): PagingSource<Int, DbContact>


    @Query("SELECT * FROM DbContact")
    abstract fun getAllContacts(): List<DbContact>
}