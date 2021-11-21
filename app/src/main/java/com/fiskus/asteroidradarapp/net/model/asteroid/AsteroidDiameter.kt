package com.fiskus.asteroidradarapp.net.model.asteroid

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

//data class for asteroid diameter from net
@JsonClass(generateAdapter = true)
data class AsteroidDiameter(
    @Json(name = "kilometers") val diameterInKilometers: AsteroidDiameterKilometers
)
