package com.fiskus.asteroidradarapp.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.fiskus.asteroidradarapp.db.NasaDatabase
import com.fiskus.asteroidradarapp.repo.AsteroidListRepo
import timber.log.Timber
import java.lang.Exception

class RefreshDataWorker(context: Context, params: WorkerParameters): CoroutineWorker(context, params) {

    companion object {
        const val WORK_NAME_REFRESH = "RefreshDataWorker"
    }

    override suspend fun doWork(): Result {
        Timber.d("doWork()")
        val repo = AsteroidListRepo(NasaDatabase.getDatabase(applicationContext))

        return try {
            //refresh cached data but don't set statuses
            repo.refreshData(false)
            Timber.d("  Result.success()")
            Result.success()
        } catch (e: Exception) {
            Timber.e("Result.retry(): ${e.message}")
            Result.retry()
        }
    }
}