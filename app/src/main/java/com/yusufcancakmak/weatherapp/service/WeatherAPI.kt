package com.yusufcancakmak.weatherapp.service

import com.yusufcancakmak.weatherapp.model.WeatherModel
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

//https://api.openweathermap.org/data/2.5/weather?q=izmir&appid=928529c03b3679abc22835ca9dd0f56e
interface WeatherAPI {
    @GET("data/2.5/weather?&units=metric&appid=928529c03b3679abc22835ca9dd0f56e")
    fun getData(
        @Query("q") cityName: String
    ): Single<WeatherModel>
}