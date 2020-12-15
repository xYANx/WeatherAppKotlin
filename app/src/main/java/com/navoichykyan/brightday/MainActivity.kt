package com.navoichykyan.brightday

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.IntentSender.SendIntentException
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.LocationSettingsResponse
import com.navoichykyan.brightday.repository.setUrl
import com.navoichykyan.brightday.weatherlist.ForecastFragment
import com.navoichykyan.brightday.weatherlist.WeatherFragment
import com.navoichykyan.brightday.weatherlist.presenter.WeatherViewInterface
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(), ViewsActivityInterface {
    private var permission: Boolean = false
    private var lat = "0.0"
    private var lon = "0.0"
    private var currentFragment = WeatherFragment.TAG
    private var locationManager: LocationManager? = null
    private val listener = MyLocationListener(this)
    private var builder: LocationSettingsRequest.Builder? = null
    private val presenter = WeatherFragment.newInstance()

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(
                arrayOf(
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ), 0
            )
            return
        } else {
            permission = true
            setBottomNavigation()
        }

        if (savedInstanceState != null) {
            currentFragment = savedInstanceState.getString("fragment", WeatherFragment.TAG)
            lat = savedInstanceState.getString("lat", "0")
            lon = savedInstanceState.getString("lon", "0")
        } else {
            if (permission) {
                addFragments()
                startLocation()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        locationManager = null
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("fragment", currentFragment)
        outState.putString("lat", lat)
        outState.putString("lon", lon)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>, grantResults: IntArray
    ) {
        when (requestCode) {
            0 -> {
                if ((grantResults.isNotEmpty() &&
                            grantResults[0] == PackageManager.PERMISSION_GRANTED)
                ) {
                    permission = true
                    setBottomNavigation()
                    addFragments()
                    startLocation()
                    Log.d("CHECK LOCATION ", "PERMISSION_GRANTED")
                } else {
                    Log.d("CHECK LOCATION ", "Unavailable")
                }
                return
            }
            else -> {
            }
        }
    }

    override fun showToast(error: String) {
        Toast.makeText(this, error, Toast.LENGTH_LONG).show()
    }

    override fun setProgressBar(view: Int) {
        progressBar.visibility = view
    }

    override fun setLocation(newLat: String, newLon: String) {
        lat = newLat
        lon = newLon
        locationManager?.removeUpdates(listener)
        locationManager = null
    }

    override fun getLocation(): Array<String> = arrayOf(lat, lon)

    @SuppressLint("MissingPermission")
    override fun setGpsProvider() {
        locationManager!!.requestLocationUpdates(
            LocationManager.GPS_PROVIDER,
            0,
            10f,
            listener
        )
    }

    override fun setGpsSettings() {
        val request = LocationRequest()
            .setFastestInterval(1500)
            .setInterval(3000)
            .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
        builder = LocationSettingsRequest.Builder()
            .addLocationRequest(request)
        LocationServices
            .getSettingsClient(this)
            .checkLocationSettings(builder!!.build())
            .addOnSuccessListener(
                this
            ) { response: LocationSettingsResponse? -> }
            .addOnFailureListener(
                this
            ) { ex: Exception? ->
                if (ex is ResolvableApiException) {
                    try {
                        val resolvable = ex
                        resolvable.startResolutionForResult(
                            this,
                            1
                        )
                    } catch (sendEx: SendIntentException) {
                    }
                }
            }
    }

    override fun update() {
        if(currentFragment == WeatherFragment.TAG)
            presenter.load()
    }

    @SuppressLint("MissingPermission")
    fun startLocation() {
        setProgressBar(View.VISIBLE)
        locationManager =
            getSystemService(LOCATION_SERVICE) as LocationManager?
        locationManager!!.requestLocationUpdates(
            LocationManager.NETWORK_PROVIDER,
            0,
            10f,
            listener
        )
        Thread(Runnable {
            kotlin.run {
                for(i in 0..10){
                    Thread.sleep(1000)
                    if (lat != "0.0" && lon != "0.0") {
                        break
                    }
                }
                if (lat == "0.0" && lon == "0.0") {
                    setLocation("51.5", "-0.1")
                    update()
                }
            }
        }).start()
    }

    private fun setBottomNavigation(){
        bottomNavigation.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.menuDay -> {
                    supportFragmentManager
                        .beginTransaction()
                        .replace(
                            R.id.fragment,
                            presenter,
                            WeatherFragment.TAG
                        )
                        .commit()
                    currentFragment = WeatherFragment.TAG
                    true
                }
                R.id.menuForecast -> {
                    supportFragmentManager
                        .beginTransaction()
                        .replace(
                            R.id.fragment,
                            ForecastFragment.newInstance(),
                            ForecastFragment.TAG
                        )
                        .commit()
                    currentFragment = ForecastFragment.TAG
                    true
                }
                else -> false
            }
        }
    }

    private fun addFragments() {
        if (currentFragment == WeatherFragment.TAG) {
            supportFragmentManager
                .beginTransaction()
                .add(
                    R.id.fragment,
                    presenter,
                    WeatherFragment.TAG
                )
                .commit()
        }
        if (currentFragment == ForecastFragment.TAG) {
            supportFragmentManager
                .beginTransaction()
                .add(
                    R.id.fragment,
                    ForecastFragment.newInstance(),
                    ForecastFragment.TAG
                )
                .commit()
        }
    }
}