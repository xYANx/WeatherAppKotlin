package com.navoichykyan.brightday.repository

import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.*
import java.io.IOException

private const val appId = "f14ff915bd7010b691e3d72c563e3a45"

fun getWeatherList(url: String): Single<List<List<WeatherDataModel>>> {
    val newsDataModelMapper: (String) -> List<List<WeatherDataModel>> =
        WeatherDataModelMapper()
    val request = Request.Builder()
        .url(url)
        .build()
    val okHttp = OkHttpClient()

    return Single.create<String> { emitter ->
        okHttp.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                emitter.onError(Throwable("Check your internet connection!"))
            }

            override fun onResponse(call: Call, response: Response) {
                if (!response.isSuccessful) {
                    emitter.onError(Throwable("Server error code: ${response.code}"))
                } else if (response.body == null) emitter.onError(Throwable("Body is null"))
                else emitter.onSuccess((response.body as ResponseBody).string())
            }
        })
    }.subscribeOn(Schedulers.io())
        .map { jsonData -> newsDataModelMapper(jsonData) }
        .observeOn(AndroidSchedulers.mainThread())

}

fun setUrl(lat: String, lon: String) =
    "https://api.openweathermap.org/data/2.5/forecast?lat=$lat&lon=$lon&units=metric&appid=$appId"
    //"https://api.openweathermap.org/data/2.5/forecast?q=Minsk&units=metric&appid=$appId"