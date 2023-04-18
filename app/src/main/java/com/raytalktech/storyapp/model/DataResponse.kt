package com.raytalktech.storyapp.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class DataResponse(
    //Default Message
    var error: Boolean,
    var message: String,

    //For Login
    var loginResult: LoginResult? = null,

    //For Feed Stories
    @SerializedName("listStory")
    var storiesResult: List<StoriesResult>? = null,

    //For Detail Feed Stories
    @SerializedName("story")
    var detailStories: StoriesResult? = null
) : Parcelable

@Parcelize
data class LoginResult(
    var userId: String,
    var name: String,
    var token: String
) : Parcelable


@Parcelize
data class StoriesResult(
    var id: String,
    var name: String,
    var description: String,
    var photoUrl: String,
    var createdAt: String,
    var lat: Double,
    var lon: Double
) : Parcelable