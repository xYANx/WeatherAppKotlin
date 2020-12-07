package com.navoichykyan.brightday.weatherlist.presenter

import com.navoichykyan.brightday.repository.WeatherDataModel

interface WeatherViewInterface {
    fun showWeatherList(list: List<List<WeatherDataModel>>)
}