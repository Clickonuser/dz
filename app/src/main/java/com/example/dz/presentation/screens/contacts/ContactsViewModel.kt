package com.example.dz.presentation.screens.contacts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dz.domain.Contact
import com.example.dz.domain.GetAllContactsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import javax.inject.Inject

data class ContactsState(
    val contacts: List<Contact> = emptyList(),
    val isLoading: Boolean = true
)

@HiltViewModel
class ContactsViewModel @Inject constructor(
    private val getAllContacts: GetAllContactsUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(ContactsState())
    val state = _state.asStateFlow()

    init { subscribe() }

    private fun subscribe() {
        getAllContacts()
            .onEach { contacts ->
                _state.update { it.copy(contacts = contacts, isLoading = false) }
            }
            .launchIn(viewModelScope)
    }
}
