package com.xabbok.nmediamaps.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.RecyclerView
import com.xabbok.nmediamaps.databinding.ListItemGeoObjectBinding
import com.xabbok.nmediamaps.dto.GeoObject
import com.xabbok.nmediamaps.presentation.viewmodels.ObjectsViewModel

class ObjectListViewAdapter(
    val context: Context,
    var dataSource: List<GeoObject>,
    val fragment: Fragment
) :
    RecyclerView.Adapter<ObjectListViewAdapter.GeoObjectViewHolder>() {
    private val viewModel: ObjectsViewModel by fragment.activityViewModels()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GeoObjectViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ListItemGeoObjectBinding.inflate(inflater, parent, false)

        return GeoObjectViewHolder(binding)
    }

    override fun onBindViewHolder(holder: GeoObjectViewHolder, position: Int) {
        val geoObject = dataSource[position]

        holder.binding.title.text = geoObject.title

        showSmallDescription(holder, geoObject)

        holder.binding.descriptionSmall.setOnClickListener { showBigDescription(holder, geoObject) }
        holder.binding.cardView.setOnClickListener { showBigDescription(holder, geoObject) }

        setupListeners(holder, geoObject)
    }

    private fun setupListeners(holder: GeoObjectViewHolder, geoObject: GeoObject) {
        holder.binding.save.setOnClickListener {
            viewModel.save(geoObject.copy(description = holder.binding.descriptionFullEdit.text.toString()))
            showSmallDescription(holder, geoObject)
        }

        holder.binding.cancel.setOnClickListener {
            showSmallDescription(holder, geoObject)
        }

        holder.binding.delete.setOnClickListener {
            showSmallDescription(holder, geoObject)
            geoObject.id?.let {id ->
                viewModel.delete(id)
            }
        }
    }

    private fun showSmallDescription(holder: GeoObjectViewHolder, geoObject: GeoObject) {
        holder.binding.descriptionSmall.text = geoObject.description

        holder.binding.descriptionSmall.visibility = View.VISIBLE
        holder.binding.descriptionFullEdit.visibility = View.GONE
        holder.binding.actionsLayout.visibility = View.GONE
    }

    private fun showBigDescription(holder: GeoObjectViewHolder, geoObject: GeoObject) {
        holder.binding.descriptionFullEdit.setText(geoObject.description)

        holder.binding.descriptionSmall.visibility = View.GONE
        holder.binding.descriptionFullEdit.visibility = View.VISIBLE
        holder.binding.actionsLayout.visibility = View.VISIBLE
    }

    override fun getItemCount(): Int {
        return dataSource.size
    }

    class GeoObjectViewHolder(val binding: ListItemGeoObjectBinding) :
        RecyclerView.ViewHolder(binding.root)

}