package com.example.dz.domain

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveContactUseCase @Inject constructor(
    private val repository: ContactsRepository
) {
    operator fun invoke(id: Int): Flow<Contact?> = repository.observeContact(id)
}
