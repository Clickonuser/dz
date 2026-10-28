package com.example.dz.presentation.screens.form

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.example.dz.R
import com.example.dz.presentation.components.ContactFields
import com.example.dz.ui.theme.AccentYellow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContactFormScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    viewModel: ContactFormViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    LaunchedEffect(state.isSaved, navController) {
        if (state.isSaved) {
            navController.popBackStack()
        }
    }

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = {
                    Text(stringResource(
                        if (!state.isEditing) R.string.create_contact else R.string.edit_contact
                    ))
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = stringResource(R.string.back))
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { viewModel.onIntent(ContactFormIntent.Save) },
                containerColor = AccentYellow
            ) {
                Icon(Icons.Default.Check, contentDescription = stringResource(R.string.save))
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 96.dp)
        ) {
            if (state.status == ContactFormStatus.Loading) {
                LinearProgressIndicator(Modifier.fillMaxWidth())
            }
            ContactFields(
                name = state.name,
                phone = state.phone,
                address = state.address,
                iconId = state.iconId,
                onNameChange = { viewModel.onIntent(ContactFormIntent.NameChanged(it)) },
                onPhoneChange = { viewModel.onIntent(ContactFormIntent.PhoneChanged(it)) },
                onAddressChange = { viewModel.onIntent(ContactFormIntent.AddressChanged(it)) },
                onIconChange = { viewModel.onIntent(ContactFormIntent.IconChanged(it)) },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}
