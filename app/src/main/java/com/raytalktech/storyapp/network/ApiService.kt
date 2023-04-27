package com.raytalktech.storyapp.network

import com.raytalktech.storyapp.model.DataResponse
import com.raytalktech.storyapp.model.StoriesResult
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface ApiService {

    @FormUrlEncoded
    @POST("login")
    fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<DataResponse>

    @FormUrlEncoded
    @POST("register")
    fun register(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<DataResponse>

    @GET("stories")
    fun allFeed(
        @Header("Authorization") token: String,
        @Query("page") page: Int? = null,
        @Query("size") size: Int? = null,
        @Query("location") location: Int? = null
    ): Call<DataResponse>

    @GET("stories")
    suspend fun allFeeds(
        @Header("Authorization") token: String,
        @Query("page") page: Int? = null,
        @Query("size") size: Int? = null,
        @Query("location") location: Int? = null
    ): DataResponse

    @GET("stories/{id}")
    fun detailFeed(
        @Header("Authorization") token: String,
        @Path("id") id: String
    ): Call<DataResponse>

    @Multipart
    @POST("stories")
    fun postStories(
        @Header("Authorization") token: String,
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody,
        @Part("lat") latitude: RequestBody,
        @Part("lon") longitude: RequestBody
    ): Call<DataResponse>
}