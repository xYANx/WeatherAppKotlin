package com.navoichykyan.brightday

interface WeatherPresenterInterface {
    fun fetchWeatherList(url: String)
    fun dispose()
}