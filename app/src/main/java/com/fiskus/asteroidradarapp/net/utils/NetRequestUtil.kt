package com.fiskus.asteroidradarapp.net.utils

import com.fiskus.asteroidradarapp.db.model.AsteroidDBModel
import com.fiskus.asteroidradarapp.net.NasaApi
import com.fiskus.asteroidradarapp.net.model.AsteroidNetModel
import com.fiskus.asteroidradarapp.net.model.asDBModel
import com.fiskus.asteroidradarapp.utils.convertDateMilliToFormattedDate
import com.squareup.moshi.Types
import org.json.JSONObject
import java.lang.reflect.Type

//parse asteroids json results to get asteroids list
fun parseAsteroidsJsonResult(jsonResult: String, datesListInMilli: List<Long>): List<AsteroidDBModel> {
    //parse json result to get near earth objects by dates
    val nearEarthObjectsJson = JSONObject(jsonResult).getJSONObject("near_earth_objects")

    //init moshi json adapter to convert json array to asteroid net model list
    val type: Type = Types.newParameterizedType(List::class.java, AsteroidNetModel::class.java)
    val moshiJsonAdapter = NasaApi.moshi.adapter<List<AsteroidNetModel>>(type)

    //init asteroids list
    val asteroidsList = mutableListOf<AsteroidDBModel>()

    //for each date get the asteroids list
    datesListInMilli.forEach { dateInMilli->
        val formattedDate = dateInMilli.convertDateMilliToFormattedDate()
        //get asteroids net model list
        val asteroidsListForDate = moshiJsonAdapter
            .fromJson(nearEarthObjectsJson.getJSONArray(formattedDate).toString())

        //add to list asteroids db model
        asteroidsListForDate?.asDBModel(dateInMilli)?.let { asteroidsList.addAll(it) }
    }

    return asteroidsList
}