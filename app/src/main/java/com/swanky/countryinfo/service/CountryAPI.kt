package com.swanky.countryinfo.service

import com.swanky.countryinfo.model.CountryData
import io.reactivex.Observable
import retrofit2.http.GET

interface CountryAPI {

    @GET("all?fields=name,capital,population,languages,currencies,maps,timezones,flags,startOfWeek")
    fun getAllCountries(): Observable<List<CountryData>>

}