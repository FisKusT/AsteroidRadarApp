package com.fiskus.asteroidradarapp.net.model

import com.fiskus.asteroidradarapp.db.model.NasaImageDB
import com.fiskus.asteroidradarapp.utils.getCurrentDateFormatted
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

const val IMAGE_MEDIA_TYPE = "image"

//data class for nasa image of the day model from net
@JsonClass(generateAdapter = true)
data class NasaImageNet(
    val explanation: String,
    val url: String,
    val title: String,
    @Json(name = "media_type") val mediaType: String
)


//convert asteroid net model list to asteroid db model list
fun NasaImageNet.asDBModel(): NasaImageDB {
    return NasaImageDB(
        url = url,
        explanation = explanation,
        title = title,
        date = getCurrentDateFormatted(),
        mediaType = mediaType
    )
}
