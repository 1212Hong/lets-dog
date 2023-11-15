package com.dog.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.dog.util.common.DataStoreManager
import com.dog.util.common.RetrofitClient
import com.dog.util.common.UploadRetrofitClient
import com.dog.util.common.userStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@InstallIn(SingletonComponent::class)
@Module
class DataStoreModule {

    @Provides
    @Singleton
    fun provideDataStoreManager(@ApplicationContext context: Context): DataStoreManager {
        return DataStoreManager(context)
    }

    @Provides
    fun provideRequestInterceptor(dataStoreManager: DataStoreManager): RetrofitClient.RequestInterceptor {
        return RetrofitClient.RequestInterceptor(dataStoreManager)
    }

    @Provides
    fun provideUploadRequestInterceptor(dataStoreManager: DataStoreManager): UploadRetrofitClient.RequestInterceptor {
        return UploadRetrofitClient.RequestInterceptor(dataStoreManager)
    }

}
