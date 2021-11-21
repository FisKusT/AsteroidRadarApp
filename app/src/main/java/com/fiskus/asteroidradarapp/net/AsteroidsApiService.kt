package com.fiskus.asteroidradarapp.net

import com.fiskus.asteroidradarapp.net.model.NasaImageNet
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

//query keys
private const val QUERY_API_KEY = "api_key"
private const val QUERY_START_DATE = "start_date"
private const val QUERY_END_DATE = "end_date"

//values
const val API_KEY = "DEMO_KEY" //Add your own API key here
private const val BASE_URL = "https://api.nasa.gov/"

//build retrofit
private val retrofit = Retrofit.Builder()
    .addConverterFactory(ScalarsConverterFactory.create()) //convert json to string
    .addConverterFactory(MoshiConverterFactory.create(NasaApi.moshi)) //convert json to object using moshi
    .baseUrl(BASE_URL)
    .build()

//nasa api service methods
interface NasaApiService {
    //get- get asteroids list
    @GET("neo/rest/v1/feed")
    suspend fun getAsteroidsJsonAsString(@Query(value = QUERY_API_KEY) apiKey: String = API_KEY,
                                         @Query(value = QUERY_START_DATE) startDate: String,
                                         @Query(value = QUERY_END_DATE) endDate: String): String
    //get- get image of the day
    @GET("planetary/apod")
    suspend fun getNasaImageOfTheDay(@Query(value = QUERY_API_KEY) apiKey: String = API_KEY): NasaImageNet
}

//object that obtain the api service retrofit instance
//using lazy to only initialize on first access
object NasaApi {
    val nasaApiService: NasaApiService by lazy {
        retrofit.create(NasaApiService::class.java)
    }

    //create moshi object
    //using lazy to only initialize on first access
    val moshi: Moshi by lazy {
        Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
    }
}
