package com.fiskus.asteroidradarapp.net.model

import com.fiskus.asteroidradarapp.db.model.AsteroidDBModel
import com.fiskus.asteroidradarapp.net.model.asteroid.AsteroidCloseApproachData
import com.fiskus.asteroidradarapp.net.model.asteroid.AsteroidDiameter
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

//data class for asteroid model from net
@JsonClass(generateAdapter = true)
data class AsteroidNetModel(
    val id: String,
    @Json(name = "absolute_magnitude_h") val absoluteMagnitude: Double,
    @Json(name = "estimated_diameter") val diameter: AsteroidDiameter,
    @Json(name = "is_potentially_hazardous_asteroid") val isHazardous: Boolean,
    @Json(name = "close_approach_data") val closeApproachData: List<AsteroidCloseApproachData>,
    val name: String
    )

//convert asteroid net model list to asteroid db model list
fun List<AsteroidNetModel>.asDBModel(dateInMilli: Long): List<AsteroidDBModel> {
    return map {
        val closeApproachData = it.closeApproachData[0]
        AsteroidDBModel(
            id = it.id,
            absoluteMagnitude = it.absoluteMagnitude,
            maxDiameter = it.diameter.diameterInKilometers.maxDiameter,
            isHazardous = it.isHazardous,
            relativeVelocityInKilometersPerSec = closeApproachData.relativeVelocity.kilometersPerSec,
            missDistanceInAstronomicalUnits = closeApproachData.missDistance.astronomical,
            name =it.name,
            dateInMilli = dateInMilli
        )
    }
}