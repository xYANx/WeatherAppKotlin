package com.navoichykyan.brightday.weatherlist

import android.annotation.SuppressLint
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
import com.navoichykyan.brightday.MyLocationListener
import com.navoichykyan.brightday.R
import com.navoichykyan.brightday.ViewsActivityInterface
import com.navoichykyan.brightday.repository.WeatherDataModel
import com.navoichykyan.brightday.repository.setUrl
import com.navoichykyan.brightday.weatherlist.presenter.WeatherPresenter
import com.navoichykyan.brightday.weatherlist.presenter.WeatherPresenterInterface
import com.navoichykyan.brightday.weatherlist.presenter.WeatherViewInterface
import kotlinx.android.synthetic.main.fragment_weather.*

class WeatherFragment : Fragment(),
    WeatherViewInterface {
    private var presenter: WeatherPresenterInterface? = null
    private var viewsActivityInterface: ViewsActivityInterface? = null
    private var activityContext: Context? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        activityContext = context
        if (context is ViewsActivityInterface) {
            viewsActivityInterface = context
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_weather, container, false)

    @SuppressLint("MissingPermission")
    override fun onResume() {
        super.onResume()
        viewsActivityInterface!!.setProgressBar(View.VISIBLE)
        presenter =
            WeatherPresenter(
                this,
                viewsActivityInterface
            )
        presenter?.fetchWeatherList(
            viewsActivityInterface!!.getUrl()
        )
    }

    override fun showWeatherList(list: List<List<WeatherDataModel>>) {
        val firstDay = list[0][0]
        textCity.text = firstDay.city
        textMainTemp.text = firstDay.temp
        textMainDescription.text = firstDay.description
        cloudsText.text = (firstDay.clouds + "%")
        windText.text = (firstDay.wind + " km/h")
        Glide.with(this)
            .load(firstDay.icon)
            .into(imageMain)
        presenter?.dispose()
    }

    companion object {
        const val TAG = "WeatherListFragment"
        fun newInstance() =
            WeatherFragment()
    }
}