package com.example.dz.presentation.screens.details

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.dz.R
import com.example.dz.ui.theme.AccentYellow
import com.example.dz.ui.theme.FieldBlue
import com.example.dz.presentation.components.ContactAvatar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContactDetailsScreen(
    onBack: () -> Unit,
    onEdit: (Int) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ContactDetailsViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val contact = state.contact
    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.contact_title)) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = stringResource(R.string.back))
                    }
                }
            )
        },
        floatingActionButton = {
            if (contact != null) {
                FloatingActionButton(onClick = { onEdit(viewModel.contactId) }, containerColor = AccentYellow) {
                    Icon(Icons.Default.Edit, contentDescription = stringResource(R.string.edit))
                }
            }
        }
    ) { innerPadding ->
        when {
            contact != null -> Column(
                modifier = Modifier.fillMaxSize().padding(innerPadding).verticalScroll(rememberScrollState())
                    .padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 96.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                ContactAvatar(iconId = contact.iconId, size = 88.dp)
                Spacer(Modifier.height(8.dp))
                DetailField(value = contact.name)
                DetailField(value = contact.phone)
                if (!contact.address.isNullOrBlank()) DetailField(value = contact.address)
            }
            !state.isLoading -> Box(
                modifier = Modifier.fillMaxSize().padding(innerPadding).padding(16.dp),
                contentAlignment = Alignment.Center
            ) { Text(stringResource(R.string.contact_not_found)) }
        }
    }
}

@Composable
private fun DetailField(value: String, modifier: Modifier = Modifier) {
    Box(modifier = modifier.fillMaxWidth().background(FieldBlue, RoundedCornerShape(12.dp)).padding(18.dp)) {
        Text(value)
    }
}
