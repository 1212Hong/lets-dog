package com.dog.util.common

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("userToken")

@Singleton
class DataStoreManager @Inject constructor(@ApplicationContext private val context: Context) {

    private val tokenDataStore = context.dataStore

    companion object {
        private val USER_TOKEN_KEY = stringPreferencesKey("user_token")
        private val FCM_TOKEN_KEY = stringPreferencesKey("fcm_token")
        private val USER_NICKNAME_KEY = stringPreferencesKey("user_nickname")
        private val USER_LOGIN_ID_KEY = stringPreferencesKey("user_login_id")
    }

    val getAccessToken: Flow<String> = context.dataStore.data.map { preferences ->
        preferences[USER_TOKEN_KEY] ?: ""
    }

    suspend fun saveToken(token: String?) {
        if (token != null) {
            Log.d("jwtToken", token)
            context.dataStore.edit { preferences ->
                preferences[USER_TOKEN_KEY] = token
            }
        }
    }

    suspend fun saveUserDetails(nickname: String?, loginId: String?) {
        context.dataStore.edit { preferences ->
            nickname?.let { preferences[USER_NICKNAME_KEY] = it }
            loginId?.let { preferences[USER_LOGIN_ID_KEY] = it }
        }
    }

    suspend fun getToken(): String {
        return context.dataStore.data.map { preferences ->
            preferences[USER_TOKEN_KEY] ?: ""
        }.first()
    }

    suspend fun getUserNickname(): String {
        return context.dataStore.data.map { preferences ->
            preferences[USER_NICKNAME_KEY] ?: ""
        }.first()
    }

    suspend fun getUserLoginId(): String {
        return context.dataStore.data.map { preferences ->
            preferences[USER_LOGIN_ID_KEY] ?: ""
        }.first()
    }


    suspend fun saveFCM(token: String?) {
        if (token != null) {
            Log.d("FCM_token", token)
            context.dataStore.edit { preferences ->
                preferences[FCM_TOKEN_KEY] = token
            }
        }
    }

    suspend fun getFCM(): String {
        return context.dataStore.data.map { preferences ->
            preferences[FCM_TOKEN_KEY] ?: ""
        }.first()
    }

    suspend fun onLogout() {
        // remove token from dataStore
        context.dataStore.edit {
            it.remove(USER_TOKEN_KEY)
        }
    }

}
