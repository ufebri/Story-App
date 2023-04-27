package com.raytalktech.storyapp.utils

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.raytalktech.storyapp.data.source.DataRepository
import com.raytalktech.storyapp.ui.addstory.AddStoryViewModel
import com.raytalktech.storyapp.ui.explore.ExploreViewModel
import com.raytalktech.storyapp.ui.home.HomeViewModel
import com.raytalktech.storyapp.ui.login.LoginViewModel
import com.raytalktech.storyapp.ui.main.MainViewModel
import com.raytalktech.storyapp.ui.profile.ProfileViewModel
import com.raytalktech.storyapp.ui.register.RegisterViewModel

class ViewModelFactory private constructor(private val dataRepository: DataRepository) :
    ViewModelProvider.NewInstanceFactory() {

    companion object {
        @Volatile
        private var instance: ViewModelFactory? = null

        fun getInstance(context: Context): ViewModelFactory = instance ?: synchronized(this) {
            instance ?: ViewModelFactory(Injection.provideRepository(context)).apply {
                instance = this
            }
        }
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        when {
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                return LoginViewModel(dataRepository) as T
            }
            modelClass.isAssignableFrom(RegisterViewModel::class.java) -> {
                return RegisterViewModel(dataRepository) as T
            }
            modelClass.isAssignableFrom(MainViewModel::class.java) -> {
                return MainViewModel(dataRepository) as T
            }
            modelClass.isAssignableFrom(HomeViewModel::class.java) -> {
                return HomeViewModel(dataRepository) as T
            }
            modelClass.isAssignableFrom(ProfileViewModel::class.java) -> {
                return ProfileViewModel(dataRepository) as T
            }
            modelClass.isAssignableFrom(AddStoryViewModel::class.java) -> {
                return AddStoryViewModel(dataRepository) as T
            }
            modelClass.isAssignableFrom(ExploreViewModel::class.java) -> {
                return ExploreViewModel(dataRepository) as T
            }
            else -> throw Throwable("Unknown ViewModel class: " + modelClass.name)
        }

    }
}