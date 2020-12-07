package com.navoichykyan.brightday.repository

import org.json.JSONObject
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class WeatherDataModelMapper : (String) -> MutableList<MutableList<WeatherDataModel>> {

    override fun invoke(jsonData: String): MutableList<MutableList<WeatherDataModel>> {
        val jsonObject = JSONObject(jsonData)
        val jsonCityArray = jsonObject.getJSONObject("city")
        val city = jsonCityArray.getString("name")
        val jsonListArray = jsonObject.getJSONArray("list")
        val listWeatherList = mutableListOf<MutableList<WeatherDataModel>>()
        var weatherList = mutableListOf<WeatherDataModel>()
        var saveDayNumber = (parseDate(
            jsonListArray.getJSONObject(0).getString("dt_txt"),
            "yy-mm-dd HH:mm:ss"
        ))!!.day
        if (jsonListArray.length() != 0) {
            for (i in 0 until jsonListArray.length()) {
                val jsonWeatherArray = jsonListArray.getJSONObject(i).getJSONArray("weather")
                val dateText = (parseDate(
                    jsonListArray.getJSONObject(i).getString("dt_txt"),
                    "yy-mm-dd HH:mm:ss"
                )?.hours.toString()) + ":00"
                val dayNumber = (parseDate(
                    jsonListArray.getJSONObject(i).getString("dt_txt"),
                    "yy-mm-dd HH:mm:ss"
                ))!!.day
                val temp = jsonListArray.getJSONObject(i).getJSONObject("main").getString("temp")
                val clouds = jsonListArray.getJSONObject(i).getJSONObject("clouds").getString("all")
                val wind = jsonListArray.getJSONObject(i).getJSONObject("wind").getString("speed")
                val dataModel = with(jsonWeatherArray.getJSONObject(0)) {
                    WeatherDataModel(
                        main = getString("main"),
                        description = getString("description"),
                        icon = "https://openweathermap.org/img/wn/" + getString("icon") + "@2x.png",
                        id = getString("id"),
                        temp = temp,
                        city = city,
                        date = dateText,
                        clouds = clouds,
                        wind = wind,
                        day = dayNumber
                    )
                }
                if(saveDayNumber == dayNumber){
                    weatherList.add(dataModel)
                } else{
                    saveDayNumber = dayNumber
                    listWeatherList.add(weatherList)
                    weatherList = mutableListOf<WeatherDataModel>()
                    weatherList.add(dataModel)
                }
            }
            listWeatherList.add(weatherList)
            return listWeatherList
        }
        return listWeatherList
    }
}

@Throws(ParseException::class)
fun parseDate(date: String, format: String): Date? {
    val formatter = SimpleDateFormat(format)
    return formatter.parse(date)
}