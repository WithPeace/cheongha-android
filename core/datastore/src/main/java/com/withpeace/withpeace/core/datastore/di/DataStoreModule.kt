package com.withpeace.withpeace.core.datastore.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataStoreModule {

    private const val AUTH_DATASTORE_NAME = "AUTH_PREFERENCES"
    private const val USER_DATASTORE_NAME = "USER_PREFERENCES"
    private const val BALANCE_GAME_DATASTORE_NAME = "BALANCE_GAME_DATASTORE_NAME"

    private val Context.authDataStore: DataStore<Preferences> by preferencesDataStore(name = AUTH_DATASTORE_NAME)
    private val Context.userDataStore: DataStore<Preferences> by preferencesDataStore(name = USER_DATASTORE_NAME)
    private val Context.balanceGameDataStore: DataStore<Preferences> by preferencesDataStore(name = BALANCE_GAME_DATASTORE_NAME)

    @Provides
    @Singleton
    @Named("auth")
    fun providesTokenDataStore(
        @ApplicationContext context: Context,
    ): DataStore<Preferences> = context.authDataStore

    @Provides
    @Singleton
    @Named("user")
    fun providesUserDataStore(
        @ApplicationContext context: Context,
    ): DataStore<Preferences> = context.userDataStore

    @Provides
    @Singleton
    @Named("balance_game")
    fun providesBalanceGameDataStore(
        @ApplicationContext context: Context,
    ): DataStore<Preferences> = context.balanceGameDataStore
}