package com.example.dz.data

import com.example.dz.domain.Contact
import com.example.dz.domain.ContactsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ContactsRepositoryImpl @Inject constructor(
    private val contactsDao: ContactsDao
) : ContactsRepository {
    override fun getAllContacts(): Flow<List<Contact>> = contactsDao.getAllContacts().map { it.toEntities() }
    override fun observeContact(id: Int): Flow<Contact?> = contactsDao.observeContact(id).map { it?.toEntity() }
    override suspend fun getContact(id: Int): Contact? = contactsDao.getContact(id)?.toEntity()
    override suspend fun addContact(contact: Contact) = contactsDao.addContact(contact.toDbModel())
    override suspend fun editContact(contact: Contact) = contactsDao.editContact(contact.toDbModel())
}
