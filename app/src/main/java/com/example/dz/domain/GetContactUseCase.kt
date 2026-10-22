package com.example.dz.domain

import javax.inject.Inject

class GetContactUseCase @Inject constructor(private val repository: ContactsRepository) {
    suspend operator fun invoke(id: Int): Contact? = repository.getContact(id)
}
