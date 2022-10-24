package com.yusufcancakmak.weatherapp.service

import com.yusufcancakmak.weatherapp.model.WeatherModel
import io.reactivex.Single
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class WeatherAPIService {

    //https://api.openweathermap.org/data/2.5/weather?q=izmir&appid=928529c03b3679abc22835ca9dd0f56e

    private val BASE_URL ="https://api.openweathermap.org/"
    private val api =Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .build()
        .create(WeatherAPI::class.java)

    fun getDataService(cityName:String): Single<WeatherModel>{
        return api.getData(cityName)

    }

}