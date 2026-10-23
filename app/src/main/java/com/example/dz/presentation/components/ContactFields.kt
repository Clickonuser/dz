package com.example.dz.presentation.components

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.dz.R
import com.example.dz.ui.theme.FieldBlue

@Composable
fun ContactFields(
    name: String,
    phone: String,
    address: String,
    iconId: Int,
    onNameChange: (String) -> Unit,
    onPhoneChange: (String) -> Unit,
    onAddressChange: (String) -> Unit,
    onIconChange: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(14.dp)) {
        Field(value = name, onValueChange = onNameChange, hint = stringResource(R.string.name))
        Field(value = phone, onValueChange = onPhoneChange, hint = stringResource(R.string.phone), keyboardType = KeyboardType.Phone)
        Field(value = address, onValueChange = onAddressChange, hint = stringResource(R.string.address_optional))
        Text(stringResource(R.string.choose_icon), color = MaterialTheme.colorScheme.onSurfaceVariant)
        Row(
            modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            avatarOptions.indices.forEach { index ->
                AvatarOption(index, iconId == index, onIconChange)
            }
        }
    }
}

@Composable
private fun AvatarOption(
    iconId: Int,
    selected: Boolean,
    onSelect: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    var avatarModifier = modifier
    if (selected) {
        avatarModifier = avatarModifier.border(3.dp, Color(0xFF2E7CB8), CircleShape)
    }
    ContactAvatar(
        iconId = iconId,
        size = 54.dp,
        modifier = avatarModifier.clickable { onSelect(iconId) }
    )
}

@Composable
private fun Field(
    value: String,
    onValueChange: (String) -> Unit,
    hint: String,
    modifier: Modifier = Modifier,
    keyboardType: KeyboardType = KeyboardType.Text
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = { Text(hint) },
        modifier = modifier.fillMaxWidth(),
        singleLine = true,
        shape = RoundedCornerShape(12.dp),
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = FieldBlue,
            unfocusedContainerColor = FieldBlue,
            focusedBorderColor = Color(0xFF4882A8),
            unfocusedBorderColor = Color(0xFF7A9BB0)
        )
    )
}
