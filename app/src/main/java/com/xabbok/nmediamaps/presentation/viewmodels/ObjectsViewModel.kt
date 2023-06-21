package com.xabbok.nmediamaps.presentation.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import com.xabbok.nmediamaps.dto.GeoObject
import com.xabbok.nmediamaps.repository.ObjectsRepository
import com.xabbok.nmediamaps.utils.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ObjectsViewModel @Inject constructor(private val repository: ObjectsRepository) :
    ViewModel() {
    val data: LiveData<List<GeoObject>> = repository.data
    val lastTouchedPoint: MutableLiveData<LatLng> = MutableLiveData()
    val currentSelectedObject: MutableLiveData<GeoObject> = MutableLiveData<GeoObject>()
    val firstMoveCameraCurrentPosition = SingleLiveEvent<Boolean>()
    private val _moveMapPosition = MutableSharedFlow<GeoObject>()
    val moveMapPosition: SharedFlow<GeoObject>
        get() = _moveMapPosition.asSharedFlow()

    init {
        repository.save(
            GeoObject(
                title = "улица Войкова",
                description = "Мытищи\nМосковская область\n2\n3\n4\n5\n6\n7\n8",
                lat = 55.918122,
                long = 37.760678,
                id = 1
            )
        )

        repository.save(
            GeoObject(
                title = "жилой комплекс Рождественский",
                description = "микрорайон имени Г.Т. Шитикова, Мытищи\nМосковская область\n2\n3\n4\n5\n6\n7\n8",
                lat = 55.919182,
                long = 37.748232,
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

    fun moveCameraToObject(value: GeoObject) {
        viewModelScope.launch {
            _moveMapPosition.emit(value)
        }
    }
}