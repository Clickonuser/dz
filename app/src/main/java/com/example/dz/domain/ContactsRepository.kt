package com.example.dz.domain

import kotlinx.coroutines.flow.Flow

interface ContactsRepository {
    fun getAllContacts(): Flow<List<Contact>>
    fun observeContact(id: Int): Flow<Contact?>
    suspend fun getContact(id: Int): Contact?
    suspend fun addContact(contact: Contact)
    suspend fun editContact(contact: Contact)
}
