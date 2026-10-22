package com.example.dz.presentation.screens.editing

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.toRoute
import com.example.dz.presentation.navigation.ScreenNavigation
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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

data class EditContactState(
    val contact: Contact? = null, val name: String = "", val phone: String = "",
    val address: String = "", val iconId: Int? = null,
    val loading: Boolean = true, val saving: Boolean = false, val finished: Boolean = false, val saveError: Boolean = false
) {
    val canSave get() = contact != null && name.isNotBlank() && phone.isNotBlank() && !saving
}

sealed interface EditContactCommand {
    data class InputName(val value: String) : EditContactCommand
    data class InputPhone(val value: String) : EditContactCommand
    data class InputAddress(val value: String) : EditContactCommand
    data class SelectIcon(val id: Int?) : EditContactCommand
    data object Save : EditContactCommand
}

@HiltViewModel
class EditContactViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getContact: GetContactUseCase,
    private val editContact: EditContactUseCase
) : ViewModel() {
    private val contactId: Int = savedStateHandle.toRoute<ScreenNavigation.EditContact>().contactId
    private val _state = MutableStateFlow(EditContactState())
    val state = _state.asStateFlow()

    init { load() }

    private fun load() {
        viewModelScope.launch {
            val contact = getContact(contactId)
            _state.update { it.copy(contact = contact, name = contact?.name.orEmpty(),
                phone = contact?.phone.orEmpty(), address = contact?.address.orEmpty(),
                iconId = contact?.iconId, loading = false) }
        }
    }

    fun processCommand(command: EditContactCommand) {
        when (command) {
            is EditContactCommand.InputName -> _state.update { it.copy(name = command.value) }
            is EditContactCommand.InputPhone -> _state.update { it.copy(phone = command.value) }
            is EditContactCommand.InputAddress -> _state.update { it.copy(address = command.value) }
            is EditContactCommand.SelectIcon -> _state.update { it.copy(iconId = command.id) }
            EditContactCommand.Save -> {
                val current = _state.value
                if (!current.canSave) return
                _state.update { it.copy(saving = true, saveError = false) }
                viewModelScope.launch {
                    try {
                        editContact(current.contact!!.copy(name = current.name.trim(),
                            phone = current.phone.trim(), address = current.address.trim().ifBlank { null },
                            iconId = current.iconId))
                        _state.update { it.copy(finished = true) }
                    } catch (e: CancellationException) {
                        throw e
                    } catch (e: Exception) {
                        _state.update { it.copy(saving = false, saveError = true) }
                    }
                }
            }
        }
    }
}
