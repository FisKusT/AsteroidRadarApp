package com.fiskus.asteroidradarapp.db.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.fiskus.asteroidradarapp.model.Asteroid
import com.fiskus.asteroidradarapp.utils.convertDateMilliToFormattedDate

//const values
const val ASTEROID_TABLE = "asteroid_table"
const val ID = "id"
const val ABSOLUTE_MAGNITUDE = "absolute_magnitude"
const val MAX_DIAMETER = "max_diameter"
const val IS_HAZARDOUS = "is_hazardous"
const val RELATIVE_VELOCITY_IN_KILOMETERS_PER_SEC = "relative_velocity_kilo_per_sec"
const val MISS_DISTANCE_IN_ASTRONOMICAL_UNITS = "miss_distance_astronomical"
const val NAME = "name"
const val DATE = "date"

//data class for asteroid model in db
@Entity(tableName = ASTEROID_TABLE)
data class AsteroidDBModel(
    @PrimaryKey
    @ColumnInfo(name = ID)
    val id: String,
    @ColumnInfo(name = ABSOLUTE_MAGNITUDE)
    val absoluteMagnitude: Double,
    @ColumnInfo(name = MAX_DIAMETER)
    val maxDiameter: Double,
    @ColumnInfo(name = IS_HAZARDOUS)
    val isHazardous: Boolean,
    @ColumnInfo(name = RELATIVE_VELOCITY_IN_KILOMETERS_PER_SEC)
    val relativeVelocityInKilometersPerSec: String,
    @ColumnInfo(name = MISS_DISTANCE_IN_ASTRONOMICAL_UNITS)
    val missDistanceInAstronomicalUnits: String,
    @ColumnInfo(name = NAME)
    val name: String,
    @ColumnInfo(name= DATE)
    val dateInMilli: Long
)

//convert asteroid db model list to asteroid model list
fun List<AsteroidDBModel>.asModel(): List<Asteroid> {
    return map {
        Asteroid(
            id = it.id,
            absoluteMagnitude = it.absoluteMagnitude,
            maxDiameter = it.maxDiameter,
            hazardous = it.isHazardous,
            relativeVelocityInKilometersPerSec = it.relativeVelocityInKilometersPerSec,
            missDistanceInAstronomicalUnits = it.missDistanceInAstronomicalUnits.toDouble(),
            name = it.name,
            formattedDate = it.dateInMilli.convertDateMilliToFormattedDate()
        )
    }
}