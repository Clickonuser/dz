package com.example.dz.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(entities = [ContactDbModel::class], version = 2, exportSchema = false)
abstract class ContactsDatabase : RoomDatabase() {
    abstract fun contactsDao(): ContactsDao
}

// Старый null становится 0, индексы существующих иконок сдвигаются на один.
val migration1To2 = object : Migration(1, 2) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("""
            CREATE TABLE contacts_new (
                id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                name TEXT NOT NULL,
                phone TEXT NOT NULL,
                address TEXT,
                iconId INTEGER NOT NULL
            )
        """.trimIndent())
        db.execSQL("""
            INSERT INTO contacts_new (id, name, phone, address, iconId)
            SELECT id, name, phone, address,
                CASE WHEN iconId BETWEEN 0 AND 4 THEN iconId + 1 ELSE 0 END
            FROM contacts
        """.trimIndent())
        db.execSQL("DROP TABLE contacts")
        db.execSQL("ALTER TABLE contacts_new RENAME TO contacts")
    }
}