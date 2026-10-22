package com.example.dz.domain

data class Contact(
    val id: Int = 0,
    val name: String,
    val phone: String,
    val address: String? = null,
    val iconId: Int? = null
)
