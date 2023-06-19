package com.xabbok.nmediamaps.presentation.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.LatLng
import com.xabbok.nmediamaps.dto.GeoObject
import com.xabbok.nmediamaps.repository.ObjectsRepository
import com.xabbok.nmediamaps.utils.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ObjectsViewModel @Inject constructor(private val repository: ObjectsRepository) :
    ViewModel() {
    val data: LiveData<List<GeoObject>> = repository.data
    val lastTouchedPoint: MutableLiveData<LatLng> = MutableLiveData()
    val firstMoveCameraCurrentPosition = SingleLiveEvent<Boolean>()

    init {
        repository.save(
            GeoObject(
                title = "Заголовок 1",
                description = "описание\n1\n2\n3\n4\n5\n6\n7\n8",
                lat = 45.0,
                long = 37.3,
                id = 1
            )
        )

        repository.save(
            GeoObject(
                title = "Заголовок 2",
                description = "описание2\n1\n2\n3\n4\n5\n6\n7\n8",
                lat = 45.0,
                long = 37.3,
                id = 2
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

    fun delete(id: Int) {
        repository.delete(id)
    }
}