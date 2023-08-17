package com.swanky.countryinfo.service

import com.swanky.countryinfo.model.CountryData
import retrofit2.Call
import retrofit2.http.GET

interface CountryAPI {

    @GET("all?fields=name,capital,population,languages,currencies,maps,timezones,flags,startOfWeek")
    fun getAllCountries(): Call<List<CountryData>>

}