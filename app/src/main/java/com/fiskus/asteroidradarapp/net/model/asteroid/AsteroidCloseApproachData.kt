package com.fiskus.asteroidradarapp.net.model.asteroid

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

//data class for asteroid close approach from net
@JsonClass(generateAdapter = true)
data class AsteroidCloseApproachData(
    @Json(name = "relative_velocity") val relativeVelocity: AsteroidRelativeVelocity,
    @Json(name = "miss_distance") val missDistance: AsteroidMissDistance
)
