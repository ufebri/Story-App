package com.raytalktech.storyapp.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.raytalktech.storyapp.data.source.DataRepository
import com.raytalktech.storyapp.data.source.remote.ApiResponse
import com.raytalktech.storyapp.model.DataResponse
import com.raytalktech.storyapp.model.UserModel
import kotlinx.coroutines.launch

class LoginViewModel(private val dataRepository: DataRepository) : ViewModel() {

    fun getLoginData(email: String, password: String): LiveData<ApiResponse<DataResponse>> =
        dataRepository.getLoginData(email, password)

    fun getUser(): LiveData<UserModel> {
        val itemsLiveData = MutableLiveData<UserModel>()
        viewModelScope.launch {
            dataRepository.getUserPref().collect {
                itemsLiveData.value = it
            }
        }
        return itemsLiveData
    }

    fun saveUser(userModel: UserModel) =
        viewModelScope.launch { dataRepository.saveUser(userModel) }
}