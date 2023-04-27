package com.raytalktech.storyapp.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.raytalktech.storyapp.data.source.DataRepository
import com.raytalktech.storyapp.data.source.remote.ApiResponse
import com.raytalktech.storyapp.model.DataResponse
import com.raytalktech.storyapp.utils.Constants.token

class HomeViewModel(private val dataRepository: DataRepository) : ViewModel() {

    fun getStoriesData(): LiveData<ApiResponse<DataResponse>> =
        dataRepository.getFeedStories(
            token, 1, 10, 1
        )

    fun getDetailStoriesData(id: String): LiveData<ApiResponse<DataResponse>> =
        dataRepository.getDetailFeedStories(token, id)
}