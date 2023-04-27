package com.raytalktech.storyapp.ui.explore

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.raytalktech.storyapp.data.source.DataRepository
import com.raytalktech.storyapp.data.source.remote.ApiResponse
import com.raytalktech.storyapp.model.DataResponse
import com.raytalktech.storyapp.utils.Constants.token

class ExploreViewModel(private val dataRepository: DataRepository) : ViewModel() {

    fun getStoriesData(): LiveData<ApiResponse<DataResponse>> =
        dataRepository.getFeedStories(token, null, null, 1)
}