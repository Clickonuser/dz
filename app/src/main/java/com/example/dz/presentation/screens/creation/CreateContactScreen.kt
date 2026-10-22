package com.example.dz.presentation.screens.creation

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
import com.example.dz.R
import com.example.dz.ui.theme.AccentYellow
import com.example.dz.presentation.components.ContactFields

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateContactScreen(
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: CreateContactViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    LaunchedEffect(state.finished) { if (state.finished) onBack() }
    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.create_contact)) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = stringResource(R.string.back))
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { viewModel.processCommand(CreateContactCommand.Save) },
                containerColor = AccentYellow
            ) { Icon(Icons.Default.Check, contentDescription = stringResource(R.string.save)) }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(innerPadding).verticalScroll(rememberScrollState())
                .padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 96.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            ContactFields(
                name = state.name,
                phone = state.phone,
                address = state.address,
                iconId = state.iconId,
                onNameChange = { viewModel.processCommand(CreateContactCommand.InputName(it)) },
                onPhoneChange = { viewModel.processCommand(CreateContactCommand.InputPhone(it)) },
                onAddressChange = { viewModel.processCommand(CreateContactCommand.InputAddress(it)) },
                onIconChange = { viewModel.processCommand(CreateContactCommand.SelectIcon(it)) },
                modifier = Modifier.fillMaxWidth()
            )
            if (state.saveError) Text(stringResource(R.string.save_error), color = MaterialTheme.colorScheme.error)
        }
    }
}
