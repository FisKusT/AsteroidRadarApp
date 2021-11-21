package com.fiskus.asteroidradarapp.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Asteroid(
    val id: String,
    val absoluteMagnitude: Double,
    val maxDiameter: Double,
    val hazardous: Boolean,
    val relativeVelocityInKilometersPerSec: String,
    val missDistanceInAstronomicalUnits: Double,
    val name: String,
    val formattedDate: String) : Parcelable
