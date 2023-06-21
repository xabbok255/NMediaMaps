package com.xabbok.nmediamaps.presentation.fragments

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.xabbok.nmediamaps.R
import com.xabbok.nmediamaps.databinding.MapInfoFragmentBinding
import com.xabbok.nmediamaps.presentation.viewmodels.ObjectsViewModel

class MapInfoFragment() : Fragment(R.layout.map_info_fragment) {
    private val viewModel: ObjectsViewModel by activityViewModels()
    private val binding: MapInfoFragmentBinding by viewBinding(MapInfoFragmentBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.currentSelectedObject.observe(viewLifecycleOwner) {
            it?.let { geoObject ->
                binding.title.text = geoObject.title
                binding.description.text = geoObject.description

                binding.editObjectButton.setOnClickListener {
                    findNavController().navigate(
                        R.id.action_global_objectEditFragment,
                        bundleOf(Pair(INTENT_EXTRA_OBJECT, geoObject))
                    )
                }
            }
        }


    }
}