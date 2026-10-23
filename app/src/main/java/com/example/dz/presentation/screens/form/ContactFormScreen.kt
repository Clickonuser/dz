package com.example.dz.presentation.screens.form

import android.widget.Toast
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.dz.R
import com.example.dz.presentation.components.ContactFields
import com.example.dz.ui.theme.AccentYellow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContactFormScreen(
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ContactFormViewModel = hiltViewModel()
) {
    val form by viewModel.form.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val errorMessage = stringResource(R.string.save_error)

    LaunchedEffect(viewModel.saveError) {
        if (viewModel.saveError) {
            Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
            viewModel.consumeSaveError()
        }
    }

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = {
                    Text(stringResource(
                        if (viewModel.contactId == null) R.string.create_contact else R.string.edit_contact
                    ))
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = stringResource(R.string.back))
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { viewModel.save(onSaved = onBack) },
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
            ContactFields(
                name = form.name,
                phone = form.phone,
                address = form.address,
                iconId = form.iconId,
                onNameChange = viewModel::changeName,
                onPhoneChange = viewModel::changePhone,
                onAddressChange = viewModel::changeAddress,
                onIconChange = viewModel::changeIcon,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}