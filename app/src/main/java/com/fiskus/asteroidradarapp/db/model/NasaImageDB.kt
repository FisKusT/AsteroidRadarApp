package com.fiskus.asteroidradarapp.db.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.fiskus.asteroidradarapp.model.NasaImage

//const values
const val NASA_IMAGE_TABLE = "nasa_image_table"
const val URL = "url"
const val EXPLANATION = "explanation"
const val TITLE = "title"
const val MEDIA_TYPE = "type"

//data class for nasa image of the day model in db
@Entity(tableName = NASA_IMAGE_TABLE)
data class NasaImageDB (
    @PrimaryKey
    @ColumnInfo(name = URL)
    val url: String,
    @ColumnInfo(name = EXPLANATION)
    val explanation: String,
    @ColumnInfo(name = TITLE)
    val title: String,
    @ColumnInfo(name = DATE)
    val date: String,
    @ColumnInfo(name = MEDIA_TYPE)
    val mediaType: String
)

//convert NasaImageDB to NasaImage
fun NasaImageDB.asModel(): NasaImage {
    return NasaImage(
        url = url,
        explanation = explanation,
        title = title,
        date = date,
        mediaType = mediaType
    )
}