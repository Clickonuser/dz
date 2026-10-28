package com.example.dz.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.dz.presentation.screens.contacts.ContactsScreen
import com.example.dz.presentation.screens.form.ContactFormScreen
import com.example.dz.presentation.screens.details.ContactDetailsScreen

@Composable
fun NavGraph() {

    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = ScreenNavigation.Contacts) {
        composable<ScreenNavigation.Contacts> { ContactsScreen(navController = navController) }
        composable<ScreenNavigation.ContactForm> { ContactFormScreen(navController = navController) }
        composable<ScreenNavigation.ContactDetails> { ContactDetailsScreen(navController = navController) }
    }
}
