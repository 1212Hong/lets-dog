package com.dog.util.common

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore

val Context.userStore: DataStore<Preferences> by preferencesDataStore(name = "user")

//val Context.accountStore: DataStore<Account> by dataStore(
//    fileName = "user_prefs.pb",
//    serializer = AccountSerializer
//)
