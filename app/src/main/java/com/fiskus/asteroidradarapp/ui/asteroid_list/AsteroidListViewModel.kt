package com.fiskus.asteroidradarapp.ui.asteroid_list

import android.content.Context
import androidx.lifecycle.*
import androidx.recyclerview.widget.RecyclerView
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.fiskus.asteroidradarapp.db.NasaDatabase
import com.fiskus.asteroidradarapp.model.Asteroid
import com.fiskus.asteroidradarapp.repo.AsteroidListRepo
import com.fiskus.asteroidradarapp.utils.convertDateMilliToFormattedDate
import com.fiskus.asteroidradarapp.utils.getConnectedConstraints
import com.fiskus.asteroidradarapp.utils.getCurrentDateListPlusXDaysInMilli
import com.fiskus.asteroidradarapp.worker.RefreshDataWorker
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

//enable dagger
@HiltViewModel
class AsteroidListViewModel @Inject constructor(@ApplicationContext context: Context) : ViewModel() {

    //init repo
    val repo = AsteroidListRepo(NasaDatabase.getDatabase(context))

    //filtered asteroid list
    val filteredAsteroidsList = MutableLiveData<List<Asteroid>>()

    var filteredValue = AsteroidListRepo.DAYS_TO_REQUEST

    //init
    init {
        Timber.d("init refreshData")
        refreshData()
    }

    //refresh data
    private fun refreshData() {
        Timber.d("refreshData")
        //refresh data
        viewModelScope.launch {
            repo.refreshData(true)
        }
    }

    //reload data
    fun reloadData() {
        loadFirstData()
        refreshData()
    }

    //load data for the first time
    fun loadFirstData() {
        repo.setStatusToLoading()
    }

    //navigation events
    //navigate to hero details event
    private val _navigateToDetails = MutableLiveData<Asteroid?>()
    val navigateToDetails: LiveData<Asteroid?>
        get() = _navigateToDetails


    //reset events
    fun resetEvents() {
        _navigateToDetails.value = null
    }

    init {
        //reset events on init
        resetEvents()
    }

    //set worker to reload data on internet connected
    fun onInternetFailure(context: Context) {
        viewModelScope.launch {
            //set one time worker constraints
            val internetFailureRequest = OneTimeWorkRequestBuilder<RefreshDataWorker>()
                .setConstraints(getConnectedConstraints())
                .build()

            //enqueue work
            WorkManager.getInstance(context)
                .enqueue(internetFailureRequest)
        }
    }

    //on asteroid click
    fun onAsteroidClick(asteroid: Asteroid) {
        //navigate to details page
        _navigateToDetails.value = asteroid
    }

    //filter asteroid list
    fun onAsteroidListFilterClick(days: Int) {
        //set filtered value
        filteredValue = days

        //formatted dates list
        val formattedDatesList = getCurrentDateListPlusXDaysInMilli(days).map { dateInMilli->
            dateInMilli.convertDateMilliToFormattedDate()
        }

        //filter asteroid display list
        filteredAsteroidsList.value = repo.asteroidsList.value?.filter { asteroid ->
            formattedDatesList.contains(asteroid.formattedDate)
        }

        repo.setStatusToDone()
    }

    //on asteroids list data change
    fun onAsteroidListDataChange(asteroidsListRV: RecyclerView) {
        //scroll to top on data change
        asteroidsListRV.scrollToPosition(0)
    }

}