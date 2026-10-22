package com.example.dz.data

import com.example.dz.domain.Contact

fun Contact.toDbModel() = ContactDbModel(id, name, phone, address, iconId)
fun ContactDbModel.toEntity() = Contact(id, name, phone, address, iconId)
fun List<ContactDbModel>.toEntities() = map { it.toEntity() }
