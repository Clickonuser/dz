package com.example.dz.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.dz.presentation.screens.contacts.ContactsScreen
import com.example.dz.presentation.screens.creation.CreateContactScreen
import com.example.dz.presentation.screens.details.ContactDetailsScreen
import com.example.dz.presentation.screens.editing.EditContactScreen

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
                    navController.navigate(ScreenNavigation.CreateContact)
                }
            )
        }
        composable<ScreenNavigation.CreateContact> {
            CreateContactScreen(
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
                    navController.navigate(ScreenNavigation.EditContact(id))
                }
            )
        }
        composable<ScreenNavigation.EditContact> {
            EditContactScreen(
                onBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}
