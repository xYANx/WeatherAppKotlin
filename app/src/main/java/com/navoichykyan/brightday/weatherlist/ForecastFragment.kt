package com.navoichykyan.brightday.weatherlist

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.navoichykyan.brightday.R
import com.navoichykyan.brightday.ViewsActivityInterface
import com.navoichykyan.brightday.repository.WeatherDataModel
import com.navoichykyan.brightday.weatherlist.adapters.WeatherAdapter
import com.navoichykyan.brightday.weatherlist.presenter.WeatherPresenter
import com.navoichykyan.brightday.weatherlist.presenter.WeatherPresenterInterface
import com.navoichykyan.brightday.weatherlist.presenter.WeatherViewInterface
import kotlinx.android.synthetic.main.fragment_forecast.*

class ForecastFragment() : Fragment(),
    WeatherViewInterface {
    private var presenter: WeatherPresenterInterface? = null
    private var viewsActivityInterface: ViewsActivityInterface? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is ViewsActivityInterface) {
            viewsActivityInterface = context
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_forecast, container, false)

    override fun onResume() {
        super.onResume()
        initWeatherList()
        presenter =
            WeatherPresenter(
                this,
                viewsActivityInterface
            )
        presenter?.fetchWeatherList(viewsActivityInterface!!.getUrl())
    }

    private fun initWeatherList() {
        viewWeatherList.apply {
            adapter =
                WeatherAdapter()
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        }
    }

    override fun showWeatherList(list: List<List<WeatherDataModel>>) {
        (viewWeatherList.adapter as WeatherAdapter).updateItemList(list)
    }

    companion object {
        const val TAG = "ForecastFragment"
        fun newInstance() =
            ForecastFragment()
    }
}