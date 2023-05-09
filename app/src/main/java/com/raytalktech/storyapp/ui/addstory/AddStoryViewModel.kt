package com.raytalktech.storyapp.ui.addstory

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.raytalktech.storyapp.data.source.DataRepository
import com.raytalktech.storyapp.data.source.remote.ApiResponse
import com.raytalktech.storyapp.model.DataResponse
import com.raytalktech.storyapp.utils.Constants.token
import okhttp3.MultipartBody

class AddStoryViewModel(private val dataRepository: DataRepository) : ViewModel() {

    fun postStories(
        file: MultipartBody.Part,
        description: String,
        latitude: Float?,
        longitude: Float?
    ): LiveData<ApiResponse<DataResponse>> =
        dataRepository.postStories(token, description, file, latitude, longitude)
}