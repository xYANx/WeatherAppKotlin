package com.navoichykyan.brightday.weatherlist.presenter

interface WeatherPresenterInterface {
    fun fetchWeatherList(url: String)
    fun dispose()
}