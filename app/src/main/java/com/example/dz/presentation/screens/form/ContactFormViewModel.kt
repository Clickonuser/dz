package com.example.dz.presentation.screens.form

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dz.domain.AddContactUseCase
import com.example.dz.domain.Contact
import com.example.dz.domain.EditContactUseCase
import com.example.dz.domain.GetContactUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ContactForm(
    val name: String = "",
    val phone: String = "",
    val address: String = "",
    val iconId: Int = 0
)

@HiltViewModel
class ContactFormViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getContact: GetContactUseCase,
    private val addContact: AddContactUseCase,
    private val editContact: EditContactUseCase
) : ViewModel() {
    val contactId: Int? = savedStateHandle["contactId"]
    private val _form = MutableStateFlow(ContactForm())
    val form = _form.asStateFlow()

    private var ready = contactId == null
    private var saving = false
    var saveError by mutableStateOf(false)
        private set

    init { load() }

    private fun load() {
        val id = contactId ?: return
        viewModelScope.launch {
            val contact = getContact(id) ?: return@launch
            _form.value = ContactForm(
                name = contact.name,
                phone = contact.phone,
                address = contact.address.orEmpty(),
                iconId = contact.iconId
            )
            ready = true
        }
    }

    fun changeName(name: String) {
        if (ready) _form.update { it.copy(name = name) }
    }

    fun changePhone(phone: String) {
        if (ready) _form.update { it.copy(phone = phone) }
    }

    fun changeAddress(address: String) {
        if (ready) _form.update { it.copy(address = address) }
    }

    fun changeIcon(iconId: Int) {
        if (ready) _form.update { it.copy(iconId = iconId) }
    }

    fun consumeSaveError() {
        saveError = false
    }

    fun save(onSaved: () -> Unit) {
        if (!ready || saving) return
        val current = _form.value
        if (current.name.isBlank() || current.phone.isBlank()) return

        saving = true
        saveError = false
        viewModelScope.launch {
            try {
                val contact = Contact(
                    id = contactId ?: 0,
                    name = current.name.trim(),
                    phone = current.phone.trim(),
                    address = current.address.trim().ifBlank { null },
                    iconId = current.iconId
                )
                if (contactId == null) {
                    addContact(contact)
                } else {
                    editContact(contact)
                }
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                saving = false
                saveError = true
                return@launch
            }
            onSaved()
        }
    }
}