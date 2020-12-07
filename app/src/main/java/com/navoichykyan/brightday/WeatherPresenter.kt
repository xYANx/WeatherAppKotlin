package com.navoichykyan.brightday

import android.view.View
import io.reactivex.disposables.Disposable

class WeatherPresenter(
    private var weatherViewInterface: WeatherViewInterface?,
    private var viewsActivityInterface: ViewsActivityInterface?
) : WeatherPresenterInterface {
    private var disposable: Disposable? = null

    override fun fetchWeatherList(url: String) {
        viewsActivityInterface!!.setProgressBar(View.VISIBLE)
        disposable = getWeatherList(url)
            .subscribe({ list ->
                weatherViewInterface!!.showWeatherList(list)
                viewsActivityInterface!!.setProgressBar(View.INVISIBLE)
            }, { throwable ->
                viewsActivityInterface!!.setProgressBar(View.INVISIBLE)
                viewsActivityInterface!!.showToast(throwable.message!!)
            })
    }

    override fun dispose() {
        disposable?.dispose()
        weatherViewInterface = null
    }

}