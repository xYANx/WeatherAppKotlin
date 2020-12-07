package com.navoichykyan.brightday.weatherlist.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.navoichykyan.brightday.R
import com.navoichykyan.brightday.getDayName
import com.navoichykyan.brightday.repository.WeatherDataModel
import kotlinx.android.synthetic.main.item_weather_forecast.view.*

class WeatherAdapter() : RecyclerView.Adapter<WeatherAdapter.WeatherItemViewHolder>() {
    private val itemList = mutableListOf<List<WeatherDataModel>>()

    class WeatherItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(listWeatherDataModel: List<WeatherDataModel>) {
            itemView.forecastList.apply {
                adapter =
                    WeatherDayAdapter()
                layoutManager = object : LinearLayoutManager(context, RecyclerView.VERTICAL, false){
                    override fun canScrollHorizontally(): Boolean {
                        return false
                    }
                }
            }
            (itemView.forecastList.adapter as WeatherDayAdapter).updateItemList(listWeatherDataModel)
            itemView.nameDay.text =
                getDayName(listWeatherDataModel[0].day)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        WeatherItemViewHolder(
            itemView = parent.run {
                LayoutInflater.from(context).inflate(
                    R.layout.item_weather_forecast,
                    this,
                    false
                )
            })

    override fun getItemCount() = itemList.size

    override fun onBindViewHolder(holder: WeatherItemViewHolder, position: Int) {
        holder.bind(itemList[position])
    }

    fun updateItemList(itemListIn: List<List<WeatherDataModel>>) {
        itemList.apply {
            clear()
            addAll(itemListIn)
        }
        notifyDataSetChanged()
    }
}