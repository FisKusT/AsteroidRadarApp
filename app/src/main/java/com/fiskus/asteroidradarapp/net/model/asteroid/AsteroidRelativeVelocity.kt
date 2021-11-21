package com.fiskus.asteroidradarapp.net.model.asteroid

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

//data class for asteroid relative velocity from net
@JsonClass(generateAdapter = true)
data class AsteroidRelativeVelocity(
    @Json(name = "kilometers_per_second") val kilometersPerSec: String
)
