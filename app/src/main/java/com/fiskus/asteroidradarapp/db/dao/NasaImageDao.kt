package com.fiskus.asteroidradarapp.db.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.fiskus.asteroidradarapp.db.model.*

//DAO interface that interacts with the Nasa Image table
@Dao
interface NasaImageDao {
    //get image
    @Query("SELECT * FROM $NASA_IMAGE_TABLE")
    fun getImage(): LiveData<NasaImageDB>

    //insert nasa image
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(image: NasaImageDB)

    //clear old image db
    @Query("DELETE FROM $NASA_IMAGE_TABLE WHERE $URL != :imageToKeepUrl")
    suspend fun clearOldImage(imageToKeepUrl: String)
}