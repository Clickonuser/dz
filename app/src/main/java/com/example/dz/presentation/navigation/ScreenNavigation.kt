package com.example.dz.presentation.navigation

import kotlinx.serialization.Serializable

sealed interface ScreenNavigation {
    @Serializable data object Contacts : ScreenNavigation
    @Serializable data class ContactDetails(val contactId: Int) : ScreenNavigation
    @Serializable data class ContactForm(val contactId: Int? = null) : ScreenNavigation
}
