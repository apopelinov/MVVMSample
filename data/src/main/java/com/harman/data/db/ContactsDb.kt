package com.harman.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.harman.data.db.model.DbContact

@Database(
    entities = [DbContact::class],
    version = 2,
    exportSchema = false
)
abstract class ContactsDb: RoomDatabase() {
    abstract fun contactDao(): ContactDao
}