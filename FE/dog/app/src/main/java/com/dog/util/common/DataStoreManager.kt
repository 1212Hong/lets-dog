package com.dog.util.common

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


class DataStoreManager(private val context: Context) {

    //    private val Context.dataStore by preferencesDataStore(name = "user_preferences")
    companion object {
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("userToken")
        private val USER_TOKEN_KEY = stringPreferencesKey("user_token")
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

    suspend fun onLogout() {
        // remove token from dataStore
        context.dataStore.edit {
            it.remove(USER_TOKEN_KEY)
        }
    }

}
