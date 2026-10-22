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
        composable<ScreenNavigation.Contacts> {
            ContactsScreen(
                onContactClick = { id ->
                    navController.navigate(ScreenNavigation.ContactDetails(id))
                },
                onAddClick = {
                    navController.navigate(ScreenNavigation.ContactForm())
                }
            )
        }
        composable<ScreenNavigation.ContactForm> {
            ContactFormScreen(
                onBack = {
                    navController.popBackStack()
                }
            )
        }
        composable<ScreenNavigation.ContactDetails> {
            ContactDetailsScreen(
                onBack = {
                    navController.popBackStack()
                },
                onEdit = { id ->
                    navController.navigate(ScreenNavigation.ContactForm(id))
                }
            )
        }
    }
}
