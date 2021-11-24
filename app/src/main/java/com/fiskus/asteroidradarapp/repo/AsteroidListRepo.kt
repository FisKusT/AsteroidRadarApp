package com.fiskus.asteroidradarapp.repo

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.fiskus.asteroidradarapp.db.NasaDatabase
import com.fiskus.asteroidradarapp.db.model.AsteroidDBModel
import com.fiskus.asteroidradarapp.db.model.asModel
import com.fiskus.asteroidradarapp.model.Asteroid
import com.fiskus.asteroidradarapp.model.NasaImage
import com.fiskus.asteroidradarapp.net.NasaApi
import com.fiskus.asteroidradarapp.net.model.asDBModel
import com.fiskus.asteroidradarapp.net.utils.parseAsteroidsJsonResult
import com.fiskus.asteroidradarapp.utils.*
import com.fiskus.asteroidradarapp.worker.RefreshDataWorker
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.lang.Exception

class AsteroidListRepo(private val nasaDatabase: NasaDatabase) {

    companion object {
        const val DAYS_TO_REQUEST = 7
    }

    //get asteroids list
    val asteroidsList: LiveData<List<Asteroid>> = Transformations.map(nasaDatabase.asteroidDao.getAsteroids()) {
        it.asModel()
    }

    //get nasa image
    val nasaImageOfTheDay: LiveData<NasaImage?> = Transformations.map(nasaDatabase.nasaImageDao.getImage()) {
        it?.asModel()
    }

    //status
    //default heroes internet request status- to be handled using the correspond binding adapter
    private val _asteroidsStatus = MutableLiveData<InternetStatus>()
    val asteroidsStatus: LiveData<InternetStatus>
        get() = _asteroidsStatus


    suspend fun refreshData(isStatusSet: Boolean) {
        refreshAsteroidsList(isStatusSet)
        refreshNasaImage()
    }

    //refresh asteroids list
    private suspend fun refreshAsteroidsList(isStatusSet: Boolean) {
        //log
        Timber.d("start refreshAsteroidsList")

        try {
            //request data from net using coroutines- IO since we doing DB changes
            withContext(Dispatchers.IO) {
                val asteroidDao = nasaDatabase.asteroidDao

                //get asteroids list from net request
                //log
                Timber.d("get asteroids list from net request")
                val datesList = getDatesListToRequest()
                val asteroidsListForDB: List<AsteroidDBModel> = parseAsteroidsJsonResult(
                    NasaApi.nasaApiService.getAsteroidsJsonAsString(startDate = datesList.first().convertDateMilliToFormattedDate(), endDate = datesList.last().convertDateMilliToFormattedDate()),
                    datesList)

                //insert asteroids to db
                //log
                Timber.d("insert asteroids to db")
                asteroidDao.insertAll(asteroidsListForDB)

                //clear previous asteroids from db
                //log
                Timber.d("clear previous asteroids from db")
                asteroidDao.clearPastAsteroids()

                //log
                Timber.d("end refreshAsteroidsList")
            }

        } catch (e: Exception) {
            //log
            Timber.e("refreshAsteroidsList error: ${e.message}")

            //network error
            //set status if there is offline caching to done else to error
            //try to set only if not called from worker
            if (isStatusSet) {
                setListStatusToErrorIfThereIsNoOfflineCache()
            } else {
                //it's called from worker needs to throw an exception to let the worker know that a failure as occourd
                throw Exception("${e.message}")
            }
        }
    }


    //get dates list for asteroid list request
    private fun getDatesListToRequest() = getCurrentDateListPlusXDaysInMilli(DAYS_TO_REQUEST)

    //refresh nasa image of the day
    private suspend fun refreshNasaImage() {
        //log
        Timber.d("start refreshNasaImage")
        try {
            //request data from net using coroutines- IO since we doing DB changes
            withContext(Dispatchers.IO) {
                //fetch new image
                //get nasa image from net request
                //log
                Timber.d("get nasa image from net request")
                val nasaImageFromNet = NasaApi.nasaApiService.getNasaImageOfTheDay()

                //get dao
                val nasaDao = nasaDatabase.nasaImageDao

                //insert nasa image to db
                //log
                Timber.d("insert nasa image to db")
                nasaDao.insert(nasaImageFromNet.asDBModel())

                //clear previous nasa image from db
                //log
                Timber.d("clear previous nasa image from db")
                nasaDao.clearOldImage(nasaImageFromNet.url)

                //log
                Timber.d("end refreshNasaImage")

            }
        } catch (e: Exception) {
            //log
            Timber.e("refreshNasaImage error: ${e.message}")

            //network error- do nothing will be handled in the list fetch
        }
    }

    //on internet failure
    fun setWorkToReloadDataOnConnection(context: Context) {
        Timber.d("start one time worker:")
        //set one time worker constraints
        val internetFailureRequest = OneTimeWorkRequestBuilder<RefreshDataWorker>()
            .setConstraints(getConnectedConstraints())
            .build()

        //enqueue work
        WorkManager.getInstance(context)
            .enqueue(internetFailureRequest)
    }

    //set asteroids list status to done if offline caching exists
    //else status is when there is no offline cache- error
    private fun setListStatusToErrorIfThereIsNoOfflineCache() {
        Timber.d("setListStatusToErrorIfThereIsNoOfflineCache:")
        //check if list is not empty
        if (asteroidsList.value?.isNotEmpty() == true) {
            //if so set status to done
            setStatusToDone()
            Timber.d("set to done")
        } else {
            //set status to else
            _asteroidsStatus.value = InternetStatus.ERROR
            Timber.d("set to error")

        }
    }

    //set status to loading
    fun setStatusToLoading() {
        Timber.d("setStatusToLoading")
        _asteroidsStatus.value = InternetStatus.LOADING
    }

    //set status to done
    fun setStatusToDone() {
        Timber.d("setStatusToDone")
        _asteroidsStatus.value = InternetStatus.DONE
    }
}