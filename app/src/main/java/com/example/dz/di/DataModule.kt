package com.example.dz.di

import android.content.Context
import androidx.room.Room
import com.example.dz.data.ContactsDao
import com.example.dz.data.ContactsDatabase
import com.example.dz.data.ContactsRepositoryImpl
import com.example.dz.domain.ContactsRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {
    @Binds
    @Singleton
    abstract fun bindContactsRepository(impl: ContactsRepositoryImpl): ContactsRepository

    companion object {
        @Provides
        @Singleton
        fun provideDatabase(@ApplicationContext context: Context): ContactsDatabase =
            Room.databaseBuilder(context, ContactsDatabase::class.java, "contacts.db").build()

        @Provides
        fun provideContactsDao(database: ContactsDatabase): ContactsDao = database.contactsDao()
    }
}
