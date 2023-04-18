package com.raytalktech.storyapp.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserModel(
    val name: String,
    val email: String,
    val password: String,
    val isLogin: Boolean,
    val token: String,
) : Parcelable
