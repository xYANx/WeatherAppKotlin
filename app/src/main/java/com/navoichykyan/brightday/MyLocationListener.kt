package com.navoichykyan.brightday

import android.location.Location
import android.location.LocationListener
import android.os.Bundle
import android.util.Log
import com.navoichykyan.brightday.repository.setUrl
import com.navoichykyan.brightday.weatherlist.WeatherFragment

class MyLocationListener(private val presenter: WeatherFragment, private val viewsActivityInterface: ViewsActivityInterface):
    LocationListener {
    override fun onLocationChanged(location: Location) {
        val text = ("" + location.longitude + ":" + location.latitude)
        Log.d("CHECK LOCATION ", text)
        presenter.loadData(setUrl(location.latitude.toString(), location.longitude.toString()))
        viewsActivityInterface.setLocation(location.latitude.toString(), location.longitude.toString())
    }
    override fun onProviderDisabled(provider: String) {
        Log.d("CHECK LOCATION ", "onProviderDisabled")
    }
    override fun onProviderEnabled(provider: String) {
        Log.d("CHECK LOCATION ", "onProviderEnabled")
    }
    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
        Log.d("CHECK LOCATION ", "onStatusChanged")
    }
}