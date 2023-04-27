package com.raytalktech.storyapp.utils

import android.content.Context
import com.raytalktech.storyapp.data.source.DataRepository
import com.raytalktech.storyapp.data.source.database.AppDatabase
import com.raytalktech.storyapp.data.source.local.UserPreference
import com.raytalktech.storyapp.data.source.remote.RemoteDataSource

object Injection {
    fun provideRepository(context: Context): DataRepository {
        val mDataStore = context.dataStore

        val remoteDataSource = RemoteDataSource.getInstance()
        val appExecutors = AppExecutors()
        val dataStore = UserPreference.getInstance(mDataStore)
        val database = AppDatabase.getDatabase(context)

        return DataRepository.getInstance(remoteDataSource, appExecutors, dataStore, database)
    }
}