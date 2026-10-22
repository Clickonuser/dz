package com.example.dz.domain

import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class GetAllContactsUseCase @Inject constructor(private val repository: ContactsRepository) {
    operator fun invoke(): Flow<List<Contact>> = repository.getAllContacts()
}
