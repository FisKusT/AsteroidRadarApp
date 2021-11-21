package com.fiskus.asteroidradarapp.net.model.asteroid

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

//data class for asteroid miss distance from net
@JsonClass(generateAdapter = true)
data class AsteroidMissDistance(
    @Json(name = "astronomical") val astronomical: String
)
