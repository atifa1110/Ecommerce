package com.example.core.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject

class DataStoreRepositoryImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>
) : DataStoreRepository {

    private object PreferencesKey {
        val onBoardingKey = booleanPreferencesKey(name = "ONBOARDING")
        val LoginKey = booleanPreferencesKey(name = "LOGIN")
        val ProfileKey = stringPreferencesKey(name = "PROFILE")
        val AccessTokenKey = stringPreferencesKey("ACCESSTOKEN")
        val RefreshTokenKey = stringPreferencesKey("REFRESHTOKEN")
        val MessagingTokenKey = stringPreferencesKey("MESSAGINGTOKEN")
    }

    override suspend fun saveOnBoardingState(complete: Boolean) {
        dataStore.edit { preferences ->
            preferences[PreferencesKey.onBoardingKey] = complete
        }
    }

    override fun getOnBoardingState(): Flow<Boolean> {
        return dataStore.data
            .catch { exception ->
                if (exception is IOException) {
                    emit(emptyPreferences())
                } else {
                    throw exception
                }
            }
            .map { preferences ->
                val onBoardingState = preferences[PreferencesKey.onBoardingKey] ?: false
                onBoardingState
            }
    }

    override suspend fun saveHasLoginState(complete: Boolean) {
        dataStore
            .edit { preferences ->
                preferences[PreferencesKey.LoginKey] = complete
            }
    }

    val isLoggedIn: Flow<Boolean> = dataStore.data
        .map { preferences ->
            preferences[PreferencesKey.LoginKey] ?: false
        }

    override fun getLoginState(): Flow<Boolean> {
        return dataStore.data
            .catch { exception ->
                if (exception is IOException) {
                    emit(emptyPreferences())
                } else {
                    throw exception
                }
            }
            .map { preferences ->
                val onLoginState = preferences[PreferencesKey.LoginKey] ?: false
                onLoginState
            }
    }

    override suspend fun saveProfileName(name: String) {
        dataStore.edit { preferences ->
            preferences[PreferencesKey.ProfileKey] = name
        }
    }

    override fun getProfileName(): Flow<String> {
        return dataStore.data
            .catch { exception ->
                if (exception is IOException) {
                    emit(emptyPreferences())
                } else {
                    throw exception
                }
            }
            .map { preferences ->
                val profileName = preferences[PreferencesKey.ProfileKey] ?: ""
                profileName
            }
    }

    override suspend fun saveAccessToken(token: String) {
        dataStore.edit { preferences ->
            preferences[PreferencesKey.AccessTokenKey] = token
        }
    }

    override fun getAccessToken(): Flow<String> {
        return dataStore.data
            .catch { exception ->
                if (exception is IOException) {
                    emit(emptyPreferences())
                } else {
                    throw exception
                }
            }
            .map { preferences ->
                val accessToken = preferences[PreferencesKey.AccessTokenKey] ?: ""
                accessToken
            }
    }

    override suspend fun saveTokenMessaging(token: String) {
        dataStore.edit { preferences ->
            preferences[PreferencesKey.MessagingTokenKey] = token
        }
    }

    override fun getTokenMessaging(): Flow<String> {
        return dataStore.data
            .catch { exception ->
                if (exception is IOException) {
                    emit(emptyPreferences())
                } else {
                    throw exception
                }
            }
            .map { preferences ->
                val messagingToken = preferences[PreferencesKey.MessagingTokenKey] ?: ""
                messagingToken
            }
    }
}
