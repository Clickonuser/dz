package com.example.dz.presentation.screens.creation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dz.domain.AddContactUseCase
import com.example.dz.domain.Contact
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class CreateContactState(
    val name: String = "", val phone: String = "", val address: String = "",
    val iconId: Int? = null, val saving: Boolean = false, val finished: Boolean = false, val saveError: Boolean = false
) {
    val canSave get() = name.isNotBlank() && phone.isNotBlank() && !saving
}

sealed interface CreateContactCommand {
    data class InputName(val value: String) : CreateContactCommand
    data class InputPhone(val value: String) : CreateContactCommand
    data class InputAddress(val value: String) : CreateContactCommand
    data class SelectIcon(val id: Int?) : CreateContactCommand
    data object Save : CreateContactCommand
}

@HiltViewModel
class CreateContactViewModel @Inject constructor(private val addContact: AddContactUseCase) : ViewModel() {
    private val _state = MutableStateFlow(CreateContactState())
    val state = _state.asStateFlow()

    fun processCommand(command: CreateContactCommand) {
        when (command) {
            is CreateContactCommand.InputName -> _state.update { it.copy(name = command.value) }
            is CreateContactCommand.InputPhone -> _state.update { it.copy(phone = command.value) }
            is CreateContactCommand.InputAddress -> _state.update { it.copy(address = command.value) }
            is CreateContactCommand.SelectIcon -> _state.update { it.copy(iconId = command.id) }
            CreateContactCommand.Save -> {
                val current = _state.value
                if (!current.canSave) return
                _state.update { it.copy(saving = true, saveError = false) }
                viewModelScope.launch {
                    try {
                        addContact(Contact(name = current.name.trim(), phone = current.phone.trim(),
                            address = current.address.trim().ifBlank { null }, iconId = current.iconId))
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
