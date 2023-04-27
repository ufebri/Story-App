package com.raytalktech.storyapp.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
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
@Entity(tableName = "story")
data class StoriesResult(
    @PrimaryKey
    @field:SerializedName("id")
    var id: String,

    @field:SerializedName("name")
    var name: String,

    @field:SerializedName("description")
    var description: String,

    @field:SerializedName("photoUrl")
    var photoUrl: String,

    @field:SerializedName("createdAt")
    var createdAt: String,

    @field:SerializedName("lat")
    var lat: Double,

    @field:SerializedName("lon")
    var lon: Double
) : Parcelable