package com.dog.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.dog.util.common.userStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent


@InstallIn(SingletonComponent::class)
@Module
class DataStoreModule {

    @Provides
    @UserDataStore
    fun providesUserDataStore(@ApplicationContext context: Context): DataStore<Preferences> {
        return context.userStore
//        return PreferenceDataStoreFactory.create(
//            corruptionHandler = ReplaceFileCorruptionHandler(
//                produceNewData = { emptyPreferences() }
//            ),
//            migrations = listOf(SharedPreferencesMigration(context, USER_PREFERENCES)),
//            scope = CoroutineScope(Dispatchers.IO + SupervisorJob()),
//            produceFile = { context.preferencesDataStoreFile(USER_PREFERENCES) }
//        )
    }
}
