package com.example.dz.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface ContactsDao {
    @Query("SELECT * FROM contacts ORDER BY name COLLATE NOCASE ASC, id ASC")
    fun getAllContacts(): Flow<List<ContactDbModel>>

    @Query("SELECT * FROM contacts WHERE id = :id LIMIT 1")
    suspend fun getContact(id: Int): ContactDbModel?

    @Query("SELECT * FROM contacts WHERE id = :id LIMIT 1")
    fun observeContact(id: Int): Flow<ContactDbModel?>

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun addContact(contact: ContactDbModel)

    @Update
    suspend fun editContact(contact: ContactDbModel)
}
