package com.example.dz.presentation.screens.details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.example.dz.domain.Contact
import com.example.dz.domain.ObserveContactUseCase
import com.example.dz.presentation.navigation.ScreenNavigation
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import javax.inject.Inject

data class ContactDetailsState(
    val contact: Contact? = null,
    val isLoading: Boolean = true
)

@HiltViewModel
class ContactDetailsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val observeContact: ObserveContactUseCase
) : ViewModel() {
    val contactId: Int = savedStateHandle.toRoute<ScreenNavigation.ContactDetails>().contactId

    private val _state = MutableStateFlow(ContactDetailsState())
    val state = _state.asStateFlow()

    init { subscribe() }

    private fun subscribe() {
        observeContact(contactId)
            .onEach { contact ->
                _state.update { it.copy(contact = contact, isLoading = false) }
            }
            .launchIn(viewModelScope)
    }
}
