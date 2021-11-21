package com.fiskus.asteroidradarapp.db.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.fiskus.asteroidradarapp.db.model.ASTEROID_TABLE
import com.fiskus.asteroidradarapp.db.model.AsteroidDBModel
import com.fiskus.asteroidradarapp.db.model.DATE
import com.fiskus.asteroidradarapp.utils.getCurrentDateStartOfDayTimeInMilli

//DAO interface that interacts with the Asteroids table
@Dao
interface AsteroidDao {

    //get all asteroids
    @Query("SELECT * FROM $ASTEROID_TABLE ORDER BY $DATE DESC")
    fun getAsteroids(): LiveData<List<AsteroidDBModel>>

    //upsert (insert and update) asteroids
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(asteroids: List<AsteroidDBModel>)

    //clear all asteroids from past days
    @Query("DELETE FROM $ASTEROID_TABLE WHERE $DATE < :currentDateStartOfDayTime")
    suspend fun clearPastAsteroids(currentDateStartOfDayTime: Long = getCurrentDateStartOfDayTimeInMilli())
}