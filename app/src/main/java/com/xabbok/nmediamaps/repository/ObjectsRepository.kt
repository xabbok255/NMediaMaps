package com.xabbok.nmediamaps.repository

import androidx.lifecycle.LiveData
import com.xabbok.nmediamaps.dto.GeoObject

interface ObjectsRepository {
    fun save(value: GeoObject)
    fun delete(id: Int)
    fun getById(id: Int) : GeoObject?

    fun loadData()

    val data: LiveData<List<GeoObject>>
}