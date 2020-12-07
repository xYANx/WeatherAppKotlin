package com.navoichykyan.brightday

data class WeatherDataModel(
    val main: String,
    val description: String,
    val icon: String,
    val date: String,
    val temp: String,
    val city: String,
    val id: String,
    val clouds: String,
    val wind: String,
    val day: Int
)