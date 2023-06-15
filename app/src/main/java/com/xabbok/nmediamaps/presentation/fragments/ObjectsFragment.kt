package com.xabbok.nmediamaps.presentation.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.xabbok.nmediamaps.R
import com.xabbok.nmediamaps.adapter.ObjectListViewAdapter
import com.xabbok.nmediamaps.databinding.FragmentObjectsBinding
import com.xabbok.nmediamaps.dto.GeoObject
import com.xabbok.nmediamaps.presentation.viewmodels.ObjectsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ObjectsFragment : Fragment(R.layout.fragment_objects) {
    private val binding: FragmentObjectsBinding by viewBinding(FragmentObjectsBinding::bind)

    private val viewModel: ObjectsViewModel by activityViewModels()
    private lateinit var adapter: ObjectListViewAdapter
    private val data: MutableList<GeoObject> = mutableListOf()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = ObjectListViewAdapter(requireContext(), data, this)
        binding.objectList.adapter = adapter
        binding.objectList.layoutManager = LinearLayoutManager(requireContext())

        setupSubscribe()
    }

    private fun setupSubscribe() {
        viewModel.data.observe(viewLifecycleOwner) {list ->

            data.clear()
            data.addAll(list)

            adapter.notifyDataSetChanged()
        }
    }
}