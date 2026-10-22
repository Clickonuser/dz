package com.example.dz.domain

import javax.inject.Inject

class EditContactUseCase @Inject constructor(private val repository: ContactsRepository) {
    suspend operator fun invoke(contact: Contact) = repository.editContact(contact)
}
