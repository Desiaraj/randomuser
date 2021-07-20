package com.example.randomuser

/**
 * Created by desiaraj on 17/07/2021
 */

// All App Constants declared here like API base url and network error message

class AppConstants {

    object Network{
        const val RandomUserAPI      = "https://randomuser.me/"
        const val WeatherAPI         = "http://api.openweathermap.org/data/2.5/"
        const val ImagePath          = "http://openweathermap.org/img/w/"
        const val APIError           = "Unable to perform request"
        const val NetworkError       = "Please check your internet connection"
        const val Timeout            = "Timeout"
        const val OpenWeatherAPIKey  = "e45747207b8d2a86924fa7d6bc47dfee";
        const val Units              = "metric"
    }
}