package com.xabbok.nmediamaps.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Marker
import com.xabbok.nmediamaps.databinding.MapInfoWindowBinding
import com.xabbok.nmediamaps.dto.GeoObject

class MapInfoWindowAdapter(mContext: Context) : GoogleMap.InfoWindowAdapter {
    private val binding: MapInfoWindowBinding = MapInfoWindowBinding.inflate(LayoutInflater.from(mContext))
    private fun setInfoWindow(marker: Marker) {
        val geoObject: GeoObject = marker.tag as GeoObject

        binding.title.text = geoObject.title
        binding.description.text = geoObject.description
    }

    override fun getInfoContents(p0: Marker): View {
        setInfoWindow(p0)
        return binding.root
    }

    override fun getInfoWindow(p0: Marker): View {
        setInfoWindow(p0)
        return binding.root
    }
}