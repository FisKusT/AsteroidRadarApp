package com.fiskus.asteroidradarapp.utils

import java.text.SimpleDateFormat
import java.util.*

//date format pattern
private const val DATE_FORMAT_PATTERN = "yyyy-MM-dd"
private const val DAY_IN_MILLI = 86400000

//get date format YYYY-MM-DD from milli
fun Long.convertDateMilliToFormattedDate(): String = getSimpleDateFormatInstance().format(this)

//get current date in date format
fun getCurrentDateFormatted(): String = getSimpleDateFormatInstance().format(getCurrentDateInMilli())

fun getCurrentDateListPlusXDaysInMilli(days: Int): List<Long> {
    //create dates list to return
    val datesListInMilli = mutableListOf(getCurrentDateInMilli())

    //run for x days
    for (day in 1 until days) {
        //get dates in milli date
        datesListInMilli.add(getNextDaysFromCurrentDayInMilli(day))
    }

    return datesListInMilli
}

//get current date in milli
fun getCurrentDateInMilli() = Calendar.getInstance().timeInMillis

//get next x days to current day
fun getNextDaysFromCurrentDayInMilli(days: Int) = getCurrentDateInMilli().plus(DAY_IN_MILLI.times(days))

//get current date start of day time
fun getCurrentDateStartOfDayTimeInMilli(): Long {
    val time = Calendar.getInstance()
    time.set(time.get(Calendar.YEAR), time.get(Calendar.MONTH), time.get(Calendar.DATE),0,0,0)
    return time.timeInMillis
}

//get simple date format instance
private fun getSimpleDateFormatInstance() = SimpleDateFormat(DATE_FORMAT_PATTERN, Locale.getDefault())
