package com.navoichykyan.brightday

interface ViewsActivityInterface {
    fun showToast(error: String)
    fun setProgressBar(view: Int)
    fun setLocation(newLat: String, newLon: String)
}