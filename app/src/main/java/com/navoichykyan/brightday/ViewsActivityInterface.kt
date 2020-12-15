package com.navoichykyan.brightday

interface ViewsActivityInterface {
    fun showToast(error: String)
    fun setProgressBar(view: Int)
    fun setLocation(newLat: String, newLon: String)
    fun getLocation(): Array<String>
    fun update()
    fun setGpsProvider()
    fun setGpsSettings()
}