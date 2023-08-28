package com.swanky.countryinfo.model

data class CountryDetails(
    val flagUrl: String,
    val startOfWeek: String,
    val countryName: String,
    val currency: String?,
    val capital: String,
    val language: String?,
    val maps: String,
    val population: Int,
    val timezones: String
) : java.io.Serializable