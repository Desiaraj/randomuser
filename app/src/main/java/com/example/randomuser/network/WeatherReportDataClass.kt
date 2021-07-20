package com.example.randomuser.network

import com.squareup.moshi.Json
import java.security.Timestamp

/**
 * Created by desiaraj on 20/07/2021
 */

//All the Weather related data classes declare here

data class WeatherReportDataClass(
    @Json(name = "weather") val list:List<Weather>?,
    @Json(name = "name") val cityName:String?,
    @Json(name = "sys") val sys: Sys,
    @Json(name = "main") val main: Main,
    @Json(name = "dt") val time:String?
)

data class Weather(
    @Json(name = "main") val main:String?,
    @Json(name = "icon") val icon:String?,
    @Json(name = "description") val desc:String?
)

data class Main(
    @Json(name = "temp") val temp:String?,
)

data class Sys(
    @Json(name = "sunrise") val sunrise:String?,
    @Json(name = "sunset") val sunset:String?
)