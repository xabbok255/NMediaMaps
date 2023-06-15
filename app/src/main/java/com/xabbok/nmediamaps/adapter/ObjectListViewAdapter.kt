package com.xabbok.nmediamaps.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.xabbok.nmediamaps.presentation.fragments.INTENT_EXTRA_OBJECT
import com.xabbok.nmediamaps.R
import com.xabbok.nmediamaps.databinding.ListItemGeoObjectBinding
import com.xabbok.nmediamaps.dto.GeoObject

class ObjectListViewAdapter(val context: Context, var dataSource: List<GeoObject>, val fragment: Fragment) :
    RecyclerView.Adapter<ObjectListViewAdapter.GeoObjectViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GeoObjectViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ListItemGeoObjectBinding.inflate(inflater, parent, false)

        return GeoObjectViewHolder(binding)
    }

    override fun onBindViewHolder(holder: GeoObjectViewHolder, position: Int) {
        val geoObject = dataSource[position]

        holder.binding.title.text = geoObject.title
        holder.binding.description.text = geoObject.description

        holder.binding.root.setOnClickListener {
            fragment.findNavController().navigate(
                R.id.action_global_objectEditFragment,
                bundleOf(Pair(INTENT_EXTRA_OBJECT, geoObject.copy()))
            )
        }
    }

    override fun getItemCount(): Int {
        return dataSource.size
    }

    class GeoObjectViewHolder(val binding: ListItemGeoObjectBinding) :
        RecyclerView.ViewHolder(binding.root)

}