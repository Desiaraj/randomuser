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
 * Created by desiaraj on 20/07/2021
 */

//This class is used for to get Weather report info for current location and random users location
class WeatherReportClient {

    private var retrofit:Retrofit

    fun getWeatherReportService(): WeatherReportService {
        return retrofit.create(WeatherReportService::class.java)
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
            .baseUrl(AppConstants.Network.WeatherAPI)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .client(okHttpClient.build())
            .build()
    }
}

interface WeatherReportService{
    @GET("weather")
    fun getWeatherByCoordinates(
        @Query("lat") lan: Double,
        @Query("lon") lon: Double,
        @Query("appid") appId: String,
        @Query("units") units: String
    ): Call<WeatherReportDataClass>
}