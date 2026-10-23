package com.example.dz.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.SentimentSatisfied
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.dz.R

val avatarOptions: List<ImageVector> = listOf(
    Icons.Default.Clear, // 0 — без иконки
    Icons.Default.Person,
    Icons.Default.Face,
    Icons.Default.AccountCircle,
    Icons.Default.SentimentSatisfied,
    Icons.Default.Star
)

@Composable
fun ContactAvatar(iconId: Int, size: Dp, modifier: Modifier = Modifier) {
    val icon = avatarOptions[iconId]
    Box(
        modifier = modifier.size(size).background(Color(0xFFE8F3FA), CircleShape)
            .border(1.dp, Color(0xFF8297A6), CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = stringResource(R.string.avatar_description),
            modifier = Modifier.size(size * 0.7f),
            tint = MaterialTheme.colorScheme.primary
        )
    }
}

@Preview(showBackground = true)
@Composable
fun Are() {
    ContactAvatar(1, 48.dp)
}