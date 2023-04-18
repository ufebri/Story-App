package com.raytalktech.storyapp.model

import com.google.gson.annotations.SerializedName

data class uploadImage(
    @SerializedName("description") val description: String,
    @SerializedName("lat") val latitude: Float,
    @SerializedName("lon") val longitude: Float
)