package com.fiskus.asteroidradarapp.net.model.asteroid

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

//data class for asteroid diameter in kilometers from net
@JsonClass(generateAdapter = true)
data class AsteroidDiameterKilometers(
    @Json(name= "estimated_diameter_max") val maxDiameter: Double

)
