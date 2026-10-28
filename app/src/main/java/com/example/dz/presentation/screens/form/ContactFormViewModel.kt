package com.example.dz.presentation.screens.form

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.example.dz.domain.AddContactUseCase
import com.example.dz.domain.Contact
import com.example.dz.domain.EditContactUseCase
import com.example.dz.domain.GetContactUseCase
import com.example.dz.presentation.navigation.ScreenNavigation
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ContactFormState(
    val name: String = "",
    val phone: String = "",
    val address: String = "",
    val iconId: Int = 0,
    val isEditing: Boolean = false,
    val status: ContactFormStatus = ContactFormStatus.Ready,
    val isSaved: Boolean = false
)

enum class ContactFormStatus { Loading, Ready, Saving, Unavailable } //

sealed interface ContactFormIntent {
    data class NameChanged(val value: String) : ContactFormIntent
    data class PhoneChanged(val value: String) : ContactFormIntent
    data class AddressChanged(val value: String) : ContactFormIntent
    data class IconChanged(val value: Int) : ContactFormIntent
    data object Save : ContactFormIntent
}

@HiltViewModel
class ContactFormViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getContact: GetContactUseCase,
    private val addContact: AddContactUseCase,
    private val editContact: EditContactUseCase
) : ViewModel() {
    private val contactId = savedStateHandle.toRoute<ScreenNavigation.ContactForm>().contactId
    private val _state = MutableStateFlow(ContactFormState(
        isEditing = contactId != null,
        status = if (contactId == null) ContactFormStatus.Ready else ContactFormStatus.Loading
    ))
    val state = _state.asStateFlow()

    init {
        contactId?.let { id ->
            viewModelScope.launch {
                try {
                    val contact = getContact(id)
                    _state.update { state ->
                        if (contact == null) {
                            state.copy(status = ContactFormStatus.Unavailable)
                        } else {
                            state.copy(
                                name = contact.name,
                                phone = contact.phone,
                                address = contact.address.orEmpty(),
                                iconId = contact.iconId,
                                status = ContactFormStatus.Ready
                            )
                        }
                    }
                } catch (e: CancellationException) {
                    throw e
                } catch (e: Exception) {
                    _state.update { it.copy(status = ContactFormStatus.Unavailable) }
                }
            }
        }
    }

    fun onIntent(intent: ContactFormIntent) {
        val status = _state.value.status
        if (status == ContactFormStatus.Loading || status == ContactFormStatus.Unavailable) return
        when (intent) {
            is ContactFormIntent.NameChanged -> _state.update { it.copy(name = intent.value) }
            is ContactFormIntent.PhoneChanged -> _state.update { it.copy(phone = intent.value) }
            is ContactFormIntent.AddressChanged -> _state.update { it.copy(address = intent.value) }
            is ContactFormIntent.IconChanged -> _state.update { it.copy(iconId = intent.value) }
            ContactFormIntent.Save -> save()
        }
    }

    private fun save() {
        val current = _state.value
        if (current.status != ContactFormStatus.Ready || current.isSaved) return
        if (current.name.isBlank() || current.phone.isBlank()) return
        _state.update { it.copy(status = ContactFormStatus.Saving) }
        viewModelScope.launch {
            try {
                val contact = Contact(
                    id = contactId ?: 0,
                    name = current.name.trim(),
                    phone = current.phone.trim(),
                    address = current.address.trim().ifBlank { null },
                    iconId = current.iconId
                )
                if (contactId == null) addContact(contact) else editContact(contact)
                _state.update { it.copy(isSaved = true) }
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _state.update { it.copy(status = ContactFormStatus.Ready) }
            }
        }
    }
}
