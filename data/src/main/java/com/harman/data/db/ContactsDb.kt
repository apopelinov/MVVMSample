package com.harman.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.harman.domain.model.Contact

@Database(
    entities = [Contact::class],
    version = 1,
    exportSchema = false
)
abstract class ContactsDb: RoomDatabase() {
    abstract fun contactDao(): ContactDao
}