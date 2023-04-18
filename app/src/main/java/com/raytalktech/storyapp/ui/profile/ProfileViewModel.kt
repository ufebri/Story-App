package com.raytalktech.storyapp.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.raytalktech.storyapp.data.source.DataRepository
import com.raytalktech.storyapp.model.UserModel
import kotlinx.coroutines.launch

class ProfileViewModel(private val dataRepository: DataRepository) : ViewModel() {

    fun getUser(): LiveData<UserModel> {
        val itemsLiveData = MutableLiveData<UserModel>()
        viewModelScope.launch {
            dataRepository.getUserPref().collect {
                itemsLiveData.value = it
            }
        }
        return itemsLiveData
    }

    fun logout() {
        viewModelScope.launch {
            dataRepository.logout()
        }
    }
}