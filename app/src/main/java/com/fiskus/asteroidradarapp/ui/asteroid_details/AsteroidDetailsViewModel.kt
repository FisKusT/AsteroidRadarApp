package com.fiskus.asteroidradarapp.ui.asteroid_details

import android.content.Context
import androidx.lifecycle.*
import com.fiskus.asteroidradarapp.R
import com.fiskus.asteroidradarapp.model.Asteroid
import com.fiskus.asteroidradarapp.utils.ShowInfoDialog
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

//enable dagger
@HiltViewModel
class AsteroidDetailsViewModel @Inject constructor(savedStateHandle: SavedStateHandle) : ViewModel() {
    companion object {
        private const val ASTEROID_ARG_KEY = "asteroid" //make sure it matches the navigation safe args
    }

    private val _asteroid = MutableLiveData<Asteroid>()
    val asteroid: LiveData<Asteroid> = _asteroid

    init {
        //get asteroid from safe args
        _asteroid.value = savedStateHandle.get<Asteroid>(ASTEROID_ARG_KEY)!!
    }

    //on astronomical unit help click
    fun onAstroUnitHelpClick(context: Context) {
        //show alert dialog with astro unit information
        ShowInfoDialog(context,
        title = context.getString(R.string.astronomical_unit_explanation_button),
        msg = context.getString(R.string.astronomical_unit_explanation))
    }

}