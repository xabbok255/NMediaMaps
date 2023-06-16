package com.xabbok.nmediamaps.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.ChangeBounds
import androidx.transition.Fade
import androidx.transition.TransitionManager
import com.xabbok.nmediamaps.databinding.ListItemGeoObjectBinding
import com.xabbok.nmediamaps.dto.GeoObject
import com.xabbok.nmediamaps.presentation.viewmodels.ObjectsViewModel

sealed class CardViewState() {
    object ReadMode : CardViewState()
    object EditMode : CardViewState()
}

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
        holder.binding.descriptionSmall.text = geoObject.description
        holder.binding.descriptionFullEdit.setText(geoObject.description)

        holder.binding.descriptionSmall.visibility = View.VISIBLE
        holder.binding.descriptionFullEdit.visibility = View.GONE
        holder.binding.actionsLayout.visibility = View.GONE
        holder.binding.cardView.tag = CardViewState.ReadMode

        holder.binding.descriptionSmall.setOnClickListener {
            toggleCardViewState(
                holder,
                geoObject
            )
        }
        holder.binding.cardView.setOnClickListener { toggleCardViewState(holder, geoObject) }

        setupListeners(holder, geoObject)
    }

    private fun toggleCardViewState(
        holder: GeoObjectViewHolder,
        geoObject: GeoObject
    ) {
        val parentLayout = holder.binding.root as ViewGroup
        val currentState = holder.binding.cardView.tag as CardViewState

        val transition = ChangeBounds()
        transition.duration = 1000

        when (currentState) {
            is CardViewState.ReadMode -> {
                // если мы находимся в режиме чтения, то переключаемся в режим редактирования
                TransitionManager.beginDelayedTransition(parentLayout, ChangeBounds())
                holder.binding.apply {
                    descriptionSmall.visibility = View.GONE
                    descriptionFullEdit.visibility = View.VISIBLE
                    actionsLayout.visibility = View.VISIBLE


                    cardView.tag =
                        CardViewState.EditMode // сохраняем новое состояние в теге CardView
                }
            }

            is CardViewState.EditMode -> {
                  // если мы находимся в режиме редактирования, то переключаемся в режим чтения
                TransitionManager.beginDelayedTransition(parentLayout, Fade())

                holder.binding.apply {
                    holder.binding.cardView.tag =
                        CardViewState.ReadMode // сохраняем новое состояние в теге CardView
                    descriptionSmall.text = geoObject.description
                    descriptionSmall.visibility = View.VISIBLE
                    descriptionFullEdit.visibility = View.GONE
                    actionsLayout.visibility = View.GONE
                    descriptionFullEdit.setText(geoObject.description)
                }
            }
        }
    }

    private fun setupListeners(holder: GeoObjectViewHolder, geoObject: GeoObject) {
        holder.binding.save.setOnClickListener {
            viewModel.save(geoObject.copy(description = holder.binding.descriptionFullEdit.text.toString()))
            toggleCardViewState(holder, geoObject)
        }

        holder.binding.cancel.setOnClickListener {
            toggleCardViewState(holder, geoObject)
        }

        holder.binding.delete.setOnClickListener {
            toggleCardViewState(holder, geoObject)
            geoObject.id?.let { id ->
                viewModel.delete(id)
            }
        }
    }

    override fun getItemCount(): Int {
        return dataSource.size
    }

    class GeoObjectViewHolder(val binding: ListItemGeoObjectBinding) :
        RecyclerView.ViewHolder(binding.root)


}