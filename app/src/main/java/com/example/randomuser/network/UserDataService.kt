package com.example.randomuser.network

import com.example.randomuser.AppConstants
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import java.util.concurrent.TimeUnit

/**
 * Created by desiaraj on 18/07/2021
 */

//This class is used for to make Random users API call and get data

class RandomUserDataClient {

    var retrofit: Retrofit

    fun getUserDataService(): UserDataService {
        return retrofit.create(UserDataService::class.java)
    }

    init {
        //initialize moshi
        val moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
        //initialize okhttp client
        val okHttpClient = OkHttpClient.Builder()
            .connectTimeout(120, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
        //initialize retrofit
        retrofit = Retrofit.Builder()
            .baseUrl(AppConstants.Network.RandomUserAPI)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .client(okHttpClient.build())
            .build()
    }
}

//This is the interface class to make API request
interface UserDataService {
    @GET("api")
    fun getUserData(@Query("results") count: Int): Call<RandomUserMain>
}
