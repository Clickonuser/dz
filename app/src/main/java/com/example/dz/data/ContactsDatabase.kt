package com.example.dz.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [ContactDbModel::class], version = 1, exportSchema = false)
abstract class ContactsDatabase : RoomDatabase() {
    abstract fun contactsDao(): ContactsDao
}
