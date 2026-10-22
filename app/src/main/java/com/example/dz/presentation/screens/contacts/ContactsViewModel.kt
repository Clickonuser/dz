package com.example.dz.presentation.screens.contacts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dz.domain.Contact
import com.example.dz.domain.GetAllContactsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

data class ContactsState(
    val contacts: List<Contact> = emptyList(),
    val isLoading: Boolean = true
)

@HiltViewModel
class ContactsViewModel @Inject constructor(
    getAllContacts: GetAllContactsUseCase
) : ViewModel() {
    val state: StateFlow<ContactsState> = getAllContacts()
        .map { contacts -> ContactsState(contacts = contacts, isLoading = false) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = ContactsState()
        )
}
