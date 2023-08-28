package com.swanky.countryinfo.model

import com.google.gson.annotations.SerializedName

data class CountryData(
    val flags: Flags,
    val startOfWeek: String,
    val name: Name,
    val currencies: Map<String, Currency>,
    val capital: List<String>,
    val languages: Map<String, String>,
    val maps: Maps,
    val population: Int,
    val timezones: List<String>
) : java.io.Serializable

data class Flags(
    val png: String,
    val svg: String,
    val alt: String
)

data class Name(
    val common: String,
    val official: String
)

data class Currency(
    val name: String,
    val symbol: String
)

data class Maps(
    val googleMaps: String,
    val openStreetMaps: String
)
