package com.example.randomuser.weatherReport

import com.example.randomuser.network.WeatherReportDataClass

/**
 * Created by desiaraj on 20/07/2021
 */

class WeatherReportContract {

    interface View{
        fun getWeatherReportSuccess(userdata: WeatherReportDataClass?)

        fun getWeatherReportFailure(msg:String)
    }

    interface Presenter{

        fun getTime(time:String):String

        fun getWeatherReport(lan:Double,lon:Double)
    }
}