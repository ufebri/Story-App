package com.raytalktech.storyapp.ui.addstory

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.raytalktech.storyapp.data.source.DataRepository
import com.raytalktech.storyapp.data.source.remote.ApiResponse
import com.raytalktech.storyapp.model.DataResponse
import com.raytalktech.storyapp.model.UserModel
import kotlinx.coroutines.launch
import okhttp3.MultipartBody

class AddStoryViewModel(private val dataRepository: DataRepository) : ViewModel() {

    fun getUser(): LiveData<UserModel> {
        val itemsLiveData = MutableLiveData<UserModel>()
        viewModelScope.launch {
            dataRepository.getUserPref().collect {
                itemsLiveData.value = it
            }
        }
        return itemsLiveData
    }

    fun postStories(
        token: String,
        file: MultipartBody.Part,
        description: String,
        latitude: Float,
        longitude: Float
    ): LiveData<ApiResponse<DataResponse>> =
        dataRepository.postStories(token, description, file, latitude, longitude)
}