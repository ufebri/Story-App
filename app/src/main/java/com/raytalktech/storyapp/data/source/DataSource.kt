package com.raytalktech.storyapp.data.source

import androidx.lifecycle.LiveData
import androidx.paging.PagingData
import com.raytalktech.storyapp.data.source.remote.ApiResponse
import com.raytalktech.storyapp.model.DataResponse
import com.raytalktech.storyapp.model.StoriesResult
import com.raytalktech.storyapp.model.UserModel
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody

interface DataSource {

    fun getLoginData(email: String, password: String): LiveData<ApiResponse<DataResponse>>

    fun getRegisterData(
        name: String,
        email: String,
        password: String
    ): LiveData<ApiResponse<DataResponse>>

    fun getUserPref(): Flow<UserModel>

    suspend fun login()

    suspend fun saveUser(userModel: UserModel)

    suspend fun logout()

    fun getFeedStories(
        token: String,
        page: Int?,
        size: Int?,
        location: Int?
    ): LiveData<ApiResponse<DataResponse>>

    fun getDetailFeedStories(token: String, id: String): LiveData<ApiResponse<DataResponse>>

    fun postStories(
        token: String,
        description: String,
        file: MultipartBody.Part,
        latitude: Float?,
        longitude: Float?
    ): LiveData<ApiResponse<DataResponse>>

    fun getAllFeed(): LiveData<PagingData<StoriesResult>>
}