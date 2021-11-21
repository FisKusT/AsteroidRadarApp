package com.fiskus.asteroidradarapp

import com.fiskus.asteroidradarapp.utils.convertDateMilliToFormattedDate
import com.fiskus.asteroidradarapp.utils.getCurrentDateListPlusXDaysInMilli
import com.fiskus.asteroidradarapp.utils.getCurrentDateStartOfDayTimeInMilli
import org.junit.Test

import org.junit.Assert.*
import java.text.SimpleDateFormat

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class DateUtilTest {

    companion object {
        private const val TIME_FORMAT = "HH-mm-ss"
        private const val ZERO_TIME = "00-00-00"
    }

    @Test
    fun testStartOfDayTime() {
        assertEquals(ZERO_TIME, SimpleDateFormat(TIME_FORMAT).format(getCurrentDateStartOfDayTimeInMilli()))
    }

    @Test
    fun testDaysFormattedList() {
        val days = 1
        val datesList = getCurrentDateListPlusXDaysInMilli(days)
        datesList.forEach{
            println(it)
            println(it.convertDateMilliToFormattedDate())
        }
        assertEquals(days, datesList.size)
    }
}