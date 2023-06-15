package com.xabbok.nmediamaps.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class GeoObject(
    val id: String? = null,
    var title: String,
    var description: String = "",
    val lat: Double,
    val long: Double
) : Parcelable

