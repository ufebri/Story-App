package com.raytalktech.storyapp.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.raytalktech.storyapp.data.source.DataRepository
import com.raytalktech.storyapp.data.source.remote.ApiResponse
import com.raytalktech.storyapp.model.DataResponse
import com.raytalktech.storyapp.model.StoriesResult
import com.raytalktech.storyapp.utils.Constants.token

class HomeViewModel(private val dataRepository: DataRepository) : ViewModel() {

    fun getDetailStoriesData(id: String): LiveData<ApiResponse<DataResponse>> =
        dataRepository.getDetailFeedStories(token, id)

    val stories: LiveData<PagingData<StoriesResult>> =
        dataRepository.getAllFeed().cachedIn(viewModelScope)
}