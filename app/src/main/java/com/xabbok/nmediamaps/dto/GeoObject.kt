package com.xabbok.nmediamaps.dto
data class GeoObject(
    val id: String? = null,
    val title: String,
    val description: String = "",
    val lat: Double,
    val long: Double
)

