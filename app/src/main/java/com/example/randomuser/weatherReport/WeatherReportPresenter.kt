package com.example.randomuser.weatherReport

import android.util.Log
import com.example.randomuser.AppConstants
import com.example.randomuser.network.WeatherReportClient
import com.example.randomuser.network.WeatherReportDataClass
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by desiaraj on 20/07/2021
 */

class WeatherReportPresenter(private val view: WeatherReportContract.View) :
    WeatherReportContract.Presenter {

    //To get Weather report
    override fun getWeatherReport(lan: Double, lon: Double) {

        WeatherReportClient().getWeatherReportService().getWeatherByCoordinates(
            lan = lan,
            lon = lon,
            appId = AppConstants.Network.OpenWeatherAPIKey,
            units = AppConstants.Network.Units
        ).enqueue(object : Callback<WeatherReportDataClass> {
            override fun onResponse(
                call: Call<WeatherReportDataClass>,
                response: Response<WeatherReportDataClass>
            ) {
                if (response.isSuccessful) {
                    view.getWeatherReportSuccess(response.body())
                } else {
                    view.getWeatherReportFailure(AppConstants.Network.APIError)
                }
            }

            override fun onFailure(call: Call<WeatherReportDataClass>, t: Throwable) {
                when (t) {
                    is ConnectException -> {
                        view.getWeatherReportFailure(AppConstants.Network.NetworkError)
                    }
                    is SocketTimeoutException -> {
                        view.getWeatherReportFailure(AppConstants.Network.Timeout)
                    }
                    else -> {
                        Log.d("Presenter", "Failure ${t.message}")
                        view.getWeatherReportFailure(AppConstants.Network.APIError)
                    }
                }
            }

        })
    }

    /**
     * To get time from weather timestamp
     * We could use this method for to get SunRiseTime and SunSet Time from given Timestamp
     */

    override fun getTime(time: String): String {

        val timeFormat = SimpleDateFormat("HH:mm:ss", Locale.ENGLISH)
        val fTime = timeFormat.format(Date(time.toLong() * 1000)).split(":").toTypedArray()

        return if (fTime[0].toInt() > 12) {
            (fTime[0].toInt() - 12).toString().plus(":").plus(fTime[1]).plus(":").plus(fTime[2])
                .plus(" PM")
        } else {
            (fTime[0].toInt()).toString().plus(":").plus(fTime[1]).plus(":").plus(fTime[2])
                .plus(" AM")
        }
    }
}