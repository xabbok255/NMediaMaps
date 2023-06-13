package com.xabbok.nmediamaps.presentation.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.xabbok.nmediamaps.dto.GeoObject
import com.xabbok.nmediamaps.repository.ObjectsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ObjectsViewModel @Inject constructor(private val repository: ObjectsRepository) :
    ViewModel() {
    val data: LiveData<List<GeoObject>> = repository.data

    init {
        repository.save(
            GeoObject(
                title = "Заголовок 1",
                description = "описание",
                lat = 45.0,
                long = 37.3,
                id = "0000000000000000"
            )
        )

        loadData()
    }

    private fun loadData() {
        repository.loadData()
    }

    fun save(value: GeoObject) {
        repository.save(value)
    }

    fun delete(id: String) {
        repository.delete(id)
    }
}