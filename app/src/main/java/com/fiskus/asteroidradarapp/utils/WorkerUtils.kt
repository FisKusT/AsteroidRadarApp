package com.fiskus.asteroidradarapp.utils

import android.os.Build
import androidx.work.Constraints
import androidx.work.NetworkType

//get connected constraints
fun getConnectedConstraints() = Constraints.Builder()
    .setRequiredNetworkType(NetworkType.CONNECTED)
    .build()


//get friendly constraints
fun getFriendlyConstraints() = Constraints.Builder()
    .setRequiredNetworkType(NetworkType.UNMETERED)
                .setRequiresBatteryNotLow(true)
                .setRequiresCharging(true)
                .apply {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        setRequiresDeviceIdle(true)
                    }
                }
    .build()