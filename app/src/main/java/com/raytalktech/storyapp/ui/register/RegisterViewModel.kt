package com.raytalktech.storyapp.ui.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.raytalktech.storyapp.data.source.DataRepository
import com.raytalktech.storyapp.data.source.remote.ApiResponse
import com.raytalktech.storyapp.model.DataResponse
import com.raytalktech.storyapp.model.UserModel
import kotlinx.coroutines.launch

class RegisterViewModel(private val dataRepository: DataRepository) : ViewModel() {

    fun getRegisterData(
        name: String,
        email: String,
        password: String
    ): LiveData<ApiResponse<DataResponse>> = dataRepository.getRegisterData(name, email, password)

    fun saveUser(userModel: UserModel) = viewModelScope.launch { dataRepository.saveUser(userModel) }
}