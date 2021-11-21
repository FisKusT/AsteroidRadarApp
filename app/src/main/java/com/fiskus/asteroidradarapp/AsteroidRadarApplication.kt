package com.fiskus.asteroidradarapp

import android.app.Application
import androidx.work.*
import com.fiskus.asteroidradarapp.utils.getFriendlyConstraints
import com.fiskus.asteroidradarapp.worker.RefreshDataWorker
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.concurrent.TimeUnit

//enable dagger hilt
@HiltAndroidApp
class AsteroidRadarApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        //init timber
        Timber.plant(Timber.DebugTree())

        //schedule work
        scheduleWork()
    }

    //schedule work
    private fun scheduleWork() {
        CoroutineScope(Dispatchers.Default).launch {
            //set worker constraints
            val constraints = getFriendlyConstraints()

            //repeat once a day
            val repeatingRequest = PeriodicWorkRequestBuilder<RefreshDataWorker>(1, TimeUnit.DAYS)
                .setConstraints(constraints)
                .build()

            //enqueue work
            WorkManager.getInstance(this@AsteroidRadarApplication)
                .enqueueUniquePeriodicWork(RefreshDataWorker.WORK_NAME_REFRESH, ExistingPeriodicWorkPolicy.KEEP, repeatingRequest)

        }
    }

}