package com.navoichykyan.brightday

import android.content.Context
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.fragment_weather.*

class WeatherFragment: Fragment(), WeatherViewInterface {
    private var presenter: WeatherPresenterInterface? = null
    private var viewsActivityInterface: ViewsActivityInterface? = null

    override fun onAttach(context: Context) {
        Log.d("WeatherFragment ", "onAttach")
        super.onAttach(context)
        if (context is ViewsActivityInterface) {
            viewsActivityInterface = context
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_weather, container, false)

    override fun onResume() {
        Log.d("WeatherFragment ", "onResume")
        super.onResume()
        presenter =
            WeatherPresenter(
                this,
                viewsActivityInterface
            )
        presenter?.fetchWeatherList(setUrl("",""))
    }

    override fun showWeatherList(list: List<List<WeatherDataModel>>) {
        textCity.text = list[0][0].city
        textMainTemp.text = list[0][0].temp
        textMainDescription.text = list[0][0].description
        cloudsText.text = (list[0][0].clouds + "%")
        windText.text = (list[0][0].wind + " km/h")
        Glide.with(this)
            .load(list[0][0].icon)
            .into(imageMain)
    }

}