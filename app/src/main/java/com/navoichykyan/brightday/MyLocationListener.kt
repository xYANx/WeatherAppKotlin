package com.navoichykyan.brightday

import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import com.navoichykyan.brightday.repository.setUrl
import com.navoichykyan.brightday.weatherlist.WeatherFragment

class MyLocationListener(private val viewsActivityInterface: ViewsActivityInterface):
    LocationListener {
    override fun onLocationChanged(location: Location) {
        viewsActivityInterface.setLocation(location.latitude.toString(), location.longitude.toString())
        viewsActivityInterface.addFragments()
    }
    override fun onProviderDisabled(provider: String) {
        viewsActivityInterface.setGpsProvider()
    }
    override fun onProviderEnabled(provider: String) {
        Log.d("CHECK LOCATION ", "onProviderEnabled")
    }
    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
        Log.d("CHECK LOCATION ", "onStatusChanged")
    }
}