package com.navoichykyan.brightday

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.navoichykyan.brightday.repository.setUrl
import com.navoichykyan.brightday.weatherlist.ForecastFragment
import com.navoichykyan.brightday.weatherlist.WeatherFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), ViewsActivityInterface {
    private var permission: Boolean = false
    private var lat = ""
    private var lon = ""
    private var currentFragment = WeatherFragment.TAG

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if(savedInstanceState != null){
            currentFragment = savedInstanceState.getString("fragment", WeatherFragment.TAG)
            lat = savedInstanceState.getString("lat", "0")
            lon = savedInstanceState.getString("lon", "0")
        }

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
        }

        if (permission) {
            if(currentFragment == WeatherFragment.TAG) {
                supportFragmentManager
                    .beginTransaction()
                    .add(
                        R.id.fragment,
                        WeatherFragment.newInstance(),
                        WeatherFragment.TAG
                    )
                    .commit()
            }
            if(currentFragment == ForecastFragment.TAG) {
                supportFragmentManager
                    .beginTransaction()
                    .add(
                        R.id.fragment,
                        ForecastFragment.newInstance(),
                        ForecastFragment.TAG
                    )
                    .commit()
            }
            bottomNavigation.setOnNavigationItemSelectedListener { item ->
                when (item.itemId) {
                    R.id.menuDay -> {
                        supportFragmentManager
                            .beginTransaction()
                            .replace(
                                R.id.fragment,
                                WeatherFragment.newInstance(),
                                WeatherFragment.TAG)
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
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("fragment", currentFragment)
        outState.putString("lat", lat)
        outState.putString("lon", lon)
    }

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            0 -> {
                permission = true
                supportFragmentManager
                    .beginTransaction()
                    .add(R.id.fragment,
                        WeatherFragment.newInstance(),
                        WeatherFragment.TAG)
                    .commit()
                if ((grantResults.isNotEmpty() &&
                            grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
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
    }

    override fun getUrl(): String = setUrl(lat, lon)
}