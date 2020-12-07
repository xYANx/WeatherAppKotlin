package com.navoichykyan.brightday

fun getDayName(idDay: Int) = when (idDay) {
    1 -> "Monday"
    2 -> "Tuesday"
    3 -> "Wednesday"
    4 -> "Thursday"
    5 -> "Friday"
    6 -> "Saturday"
    0 -> "Sunday"
    else -> "Day"
}