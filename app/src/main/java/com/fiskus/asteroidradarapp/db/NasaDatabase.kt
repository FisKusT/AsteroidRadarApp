package com.fiskus.asteroidradarapp.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.fiskus.asteroidradarapp.db.dao.AsteroidDao
import com.fiskus.asteroidradarapp.db.dao.NasaImageDao
import com.fiskus.asteroidradarapp.db.model.AsteroidDBModel
import com.fiskus.asteroidradarapp.db.model.NasaImageDB

//database abstract class to create room database for offline caching
@Database(entities = [AsteroidDBModel::class, NasaImageDB::class], version = 1, exportSchema = false)
abstract class NasaDatabase: RoomDatabase() {
    //dao
    abstract val asteroidDao: AsteroidDao
    abstract val nasaImageDao: NasaImageDao

    //object to hold the DB
    companion object {
        @Volatile
        private lateinit var INSTANCE: NasaDatabase

        private const val DB_NAME = "database_nasa"

        //get database instance
        fun getDatabase(context: Context): NasaDatabase {
            synchronized(this) {
                if (!::INSTANCE.isInitialized) {
                    INSTANCE = Room.databaseBuilder(context.applicationContext, NasaDatabase::class.java, DB_NAME)
                        .fallbackToDestructiveMigration()
                        .build()
                }
                return INSTANCE
            }
        }
    }
}