package com.navoichykyan.brightday.weatherlist.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.navoichykyan.brightday.R
import com.navoichykyan.brightday.repository.WeatherDataModel
import kotlinx.android.synthetic.main.item_weather.view.*

class WeatherDayAdapter: RecyclerView.Adapter<WeatherDayAdapter.WeatherDayItemViewHolder>() {
    private val itemList = mutableListOf<WeatherDataModel>()
    class WeatherDayItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(weatherDataModel: WeatherDataModel) {
            with(weatherDataModel) {
                itemView.apply {
                    textTime.text = date
                    textDescription.text = description
                    textTemp.text = temp
                    Glide.with(itemView.context)
                        .load(icon)
                        .into(itemView.image_weather)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        WeatherDayItemViewHolder(
            itemView = parent.run {
                LayoutInflater.from(context).inflate(
                    R.layout.item_weather,
                    this,
                    false
                )
            })

    override fun getItemCount() = itemList.size

    fun updateItemList(itemListIn: List<WeatherDataModel>) {
        itemList.apply {
            clear()
            addAll(itemListIn)
        }
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: WeatherDayItemViewHolder, position: Int) {
        holder.bind(itemList[position])
    }
}