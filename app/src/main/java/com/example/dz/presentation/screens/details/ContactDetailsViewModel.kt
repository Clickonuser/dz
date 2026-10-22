package com.example.dz.presentation.screens.details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.example.dz.domain.Contact
import com.example.dz.domain.ObserveContactUseCase
import com.example.dz.presentation.navigation.ScreenNavigation
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

data class ContactDetailsState(
    val contact: Contact? = null,
    val isLoading: Boolean = true
)

@HiltViewModel
class ContactDetailsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    observeContact: ObserveContactUseCase
) : ViewModel() {
    val contactId: Int = savedStateHandle.toRoute<ScreenNavigation.ContactDetails>().contactId

    val state: StateFlow<ContactDetailsState> = observeContact(contactId)
        .map { contact -> ContactDetailsState(contact = contact, isLoading = false) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = ContactDetailsState()
        )
}
