package com.xabbok.nmediamaps.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.xabbok.nmediamaps.dto.GeoObject
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class ObjectsRepositorySharedPrefsImpl @Inject constructor(
    @ApplicationContext
    context: Context
) : ObjectsRepository {
    private val sharedPreferences =
        context.getSharedPreferences("repository_objects", Context.MODE_PRIVATE)

    private val _data: MutableLiveData<List<GeoObject>> =
        MutableLiveData<List<GeoObject>>(emptyList())

    override val data: LiveData<List<GeoObject>>
        get() {
            return _data
        }

    private val gson = Gson()

    override fun save(value: GeoObject) {

        val newValue =
            value.copy(
                id = if (value.id != 0)
                    value.id
                else
                    _data.value?.maxByOrNull { it.id }?.id?.plus(1) ?: 1
            )
        val json = gson.toJson(newValue)
        sharedPreferences.edit().putString(newValue.id.toString(), json).apply()
        loadData()
    }

    override fun delete(id: Int) {
        sharedPreferences.edit().remove(id.toString()).apply()
        loadData()
    }

    override fun getById(id: Int): GeoObject? {
        val json = sharedPreferences.getString(id.toString(), null)
        return json?.let { gson.fromJson(it, GeoObject::class.java) }
    }

    override fun loadData() {
        val result = mutableListOf<GeoObject>()
        for (key in sharedPreferences.all.keys) {
            val json = sharedPreferences.getString(key, null)
            if (json != null) {
                val data = gson.fromJson(json, GeoObject::class.java)
                result.add(data)
            }
        }

        _data.postValue(result)
    }
}