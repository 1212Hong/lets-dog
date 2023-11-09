package com.dog.util.common

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class UserService @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {

    // returns a flow of is authenticated state
    fun isAuthenticated(): Flow<Boolean> {
        // flow of token existence from dataStore
        return dataStore.data.map {
            it.contains(KEY_TOKEN)
        }
    }

    // store new token after sign in or token refresh
    suspend fun store(token: String) {
        // store token to dataStore
        dataStore.edit {
            it[KEY_TOKEN] = token
        }
    }

    // get token for protected API method
    suspend fun getToken(): String {
        return dataStore.data
            .map { it[KEY_TOKEN] } // get a flow of token from dataStore
            .firstOrNull() // transform flow to suspend
            ?: throw IllegalArgumentException("no token stored")
    }

    // to call when user logs out or when refreshing the token has failed
    suspend fun onLogout() {
        // remove token from dataStore
        dataStore.edit {
            it.remove(KEY_TOKEN)
        }
    }

    companion object {
        val KEY_TOKEN = stringPreferencesKey("key_token")
    }
}
