package com.raytalktech.storyapp.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.raytalktech.storyapp.data.source.DataRepository
import com.raytalktech.storyapp.data.source.remote.ApiResponse
import com.raytalktech.storyapp.model.DataResponse
import com.raytalktech.storyapp.model.UserModel
import kotlinx.coroutines.launch

class HomeViewModel(private val dataRepository: DataRepository) : ViewModel() {


    fun getUser(): LiveData<UserModel> {
        val itemsLiveData = MutableLiveData<UserModel>()
        viewModelScope.launch {
            dataRepository.getUserPref().collect {
                itemsLiveData.value = it
            }
        }
        return itemsLiveData
    }

    fun getStoriesData(token: String): LiveData<ApiResponse<DataResponse>> =
        dataRepository.getFeedStories(
            token, 1, 10, 1
        )

    fun getDetailStoriesData(token: String, id: String): LiveData<ApiResponse<DataResponse>> =
        dataRepository.getDetailFeedStories(token, id)
}