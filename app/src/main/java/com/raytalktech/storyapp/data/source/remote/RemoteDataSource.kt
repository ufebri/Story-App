package com.raytalktech.storyapp.data.source.remote

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.raytalktech.storyapp.model.DataResponse
import com.raytalktech.storyapp.network.ApiConfig
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RemoteDataSource {

    val client = ApiConfig.getApiService()

    companion object {
        private const val TAG = "RemoteDataSource"


        @Volatile
        private var instance: RemoteDataSource? = null

        fun getInstance(): RemoteDataSource = instance ?: synchronized(this) {
            instance ?: RemoteDataSource()
        }
    }

    private fun getLog(message: String) {
        Log.d(TAG, message)
    }

    fun loginApp(email: String, password: String): LiveData<ApiResponse<DataResponse>> {
        val resultData = MutableLiveData<ApiResponse<DataResponse>>()
        client.login(email, password).enqueue(object : Callback<DataResponse> {
            override fun onResponse(call: Call<DataResponse>, response: Response<DataResponse>) {
                if (response.body() != null) resultData.value =
                    ApiResponse.success(response.body() as DataResponse)
                else if (response.errorBody() != null) resultData.value = ApiResponse.error(
                    response.message(), DataResponse(true, response.message())
                )
                else resultData.value =
                    ApiResponse.empty(response.message(), response.body() as DataResponse)
                getLog("onResponse: " + response.raw())
            }

            override fun onFailure(call: Call<DataResponse>, t: Throwable) {
                getLog("onFailure: ${t.message}")
            }

        })
        return resultData
    }

    fun registerApp(
        name: String, email: String, password: String
    ): LiveData<ApiResponse<DataResponse>> {
        val resultData = MutableLiveData<ApiResponse<DataResponse>>()
        client.register(name, email, password).enqueue(object : Callback<DataResponse> {
            override fun onResponse(call: Call<DataResponse>, response: Response<DataResponse>) {
                if (response.body() != null) resultData.value =
                    ApiResponse.success(response.body() as DataResponse)
                else if (response.errorBody() != null) resultData.value = ApiResponse.error(
                    response.message(), DataResponse(true, response.message())
                )
                else resultData.value =
                    ApiResponse.empty(response.message(), response.body() as DataResponse)
                getLog("onResponse: " + response.raw())
            }

            override fun onFailure(call: Call<DataResponse>, t: Throwable) {
                getLog("onFailure: ${t.message}")
            }
        })

        return resultData
    }


    fun storiesFeed(
        token: String, page: Int?, size: Int?, location: Int?
    ): LiveData<ApiResponse<DataResponse>> {
        val resultData = MutableLiveData<ApiResponse<DataResponse>>()
        client.allFeed("bearer $token", page, size, location)
            .enqueue(object : Callback<DataResponse> {
                override fun onResponse(
                    call: Call<DataResponse>, response: Response<DataResponse>
                ) {
                    if (response.body() != null) resultData.value =
                        ApiResponse.success(response.body() as DataResponse)
                    else if (response.errorBody() != null) resultData.value = ApiResponse.error(
                        response.message(), DataResponse(true, response.message())
                    )
                    else resultData.value =
                        ApiResponse.empty(response.message(), response.body() as DataResponse)
                    getLog("onResponse: " + response.raw())
                }

                override fun onFailure(call: Call<DataResponse>, t: Throwable) {
                    getLog("onFailure: ${t.message}")
                }
            })
        return resultData
    }

    fun detailFeed(token: String, id: String): LiveData<ApiResponse<DataResponse>> {
        val resultData = MutableLiveData<ApiResponse<DataResponse>>()
        client.detailFeed("bearer $token", id).enqueue(object : Callback<DataResponse> {
            override fun onResponse(call: Call<DataResponse>, response: Response<DataResponse>) {
                if (response.body() != null) resultData.value =
                    ApiResponse.success(response.body() as DataResponse)
                else if (response.errorBody() != null) resultData.value = ApiResponse.error(
                    response.message(), DataResponse(true, response.message())
                )
                else resultData.value =
                    ApiResponse.empty(response.message(), response.body() as DataResponse)
                getLog("onResponse: " + response.raw())
            }

            override fun onFailure(call: Call<DataResponse>, t: Throwable) {
                getLog("onFailure: ${t.message}")
            }
        })
        return resultData
    }

    fun postStories(
        token: String,
        file: MultipartBody.Part,
        description: String,
        latitude: Float,
        longitude: Float
    ): LiveData<ApiResponse<DataResponse>> {
        val resultData = MutableLiveData<ApiResponse<DataResponse>>()

        client.postStories(
            "bearer $token",
            file,
            description.toRequestBody(MultipartBody.FORM),
            latitude.toString().toRequestBody(MultipartBody.FORM),
            longitude.toString().toRequestBody(MultipartBody.FORM)
        ).enqueue(object : Callback<DataResponse> {
                override fun onResponse(
                    call: Call<DataResponse>, response: Response<DataResponse>
                ) {
                    if (response.body() != null) resultData.value =
                        ApiResponse.success(response.body() as DataResponse)
                    else if (response.errorBody() != null) resultData.value = ApiResponse.error(
                        response.message(), DataResponse(true, response.message())
                    )
                    else resultData.value =
                        ApiResponse.empty(response.message(), response.body() as DataResponse)
                    getLog("onResponse: " + response.raw())
                }

                override fun onFailure(call: Call<DataResponse>, t: Throwable) {
                    getLog("onFailure: ${t.message}")
                }
            })
        return resultData
    }
}