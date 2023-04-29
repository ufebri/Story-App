package com.raytalktech.storyapp.data.source

import androidx.lifecycle.LiveData
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.raytalktech.storyapp.data.source.database.AppDatabase
import com.raytalktech.storyapp.data.source.database.AppRemoteMediator
import com.raytalktech.storyapp.data.source.local.UserPreference
import com.raytalktech.storyapp.data.source.remote.ApiResponse
import com.raytalktech.storyapp.data.source.remote.RemoteDataSource
import com.raytalktech.storyapp.model.DataResponse
import com.raytalktech.storyapp.model.StoriesResult
import com.raytalktech.storyapp.model.UserModel
import com.raytalktech.storyapp.utils.AppExecutors
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody

class DataRepository private constructor(
    private val appExecutors: AppExecutors,
    private val remoteDataSource: RemoteDataSource,
    private val dataStore: UserPreference,
    private val appDatabase: AppDatabase
) : DataSource {

    companion object {
        @Volatile
        private var instance: DataRepository? = null

        fun getInstance(
            remoteData: RemoteDataSource,
            appExecutors: AppExecutors,
            userPreference: UserPreference,
            appDatabase: AppDatabase
        ): DataRepository = instance ?: synchronized(this) {
            instance ?: DataRepository(
                appExecutors, remoteData, userPreference, appDatabase
            )
        }
    }

    override fun getLoginData(
        email: String, password: String
    ): LiveData<ApiResponse<DataResponse>> {
        return remoteDataSource.loginApp(email, password)
    }

    override fun getRegisterData(
        name: String, email: String, password: String
    ): LiveData<ApiResponse<DataResponse>> {
        return remoteDataSource.registerApp(name, email, password)
    }

    override fun getUserPref(): Flow<UserModel> {
        return dataStore.getUser()
    }

    override suspend fun login() = dataStore.login()

    override suspend fun saveUser(userModel: UserModel) = dataStore.saveUser(userModel)

    override suspend fun logout() = dataStore.logout()

    override fun getFeedStories(
        token: String, page: Int?, size: Int?, location: Int?
    ): LiveData<ApiResponse<DataResponse>> {
        return remoteDataSource.storiesFeed(token, page, size, location)
    }

    override fun getDetailFeedStories(
        token: String, id: String
    ): LiveData<ApiResponse<DataResponse>> {
        return remoteDataSource.detailFeed(token, id)
    }

    override fun postStories(
        token: String,
        description: String,
        file: MultipartBody.Part,
        latitude: Float,
        longitude: Float
    ): LiveData<ApiResponse<DataResponse>> {
        return remoteDataSource.postStories(token, file, description, latitude, longitude)
    }

    override fun getAllFeed(): LiveData<PagingData<StoriesResult>> {
        @OptIn(ExperimentalPagingApi::class)
        return Pager(
            config = PagingConfig(pageSize = 5),
            remoteMediator = AppRemoteMediator(appDatabase, remoteDataSource.client),
            pagingSourceFactory = { //AppPagingSource(remoteDataSource.client)
                appDatabase.appDao().getAllQuote()
            }
        ).liveData
    }
}