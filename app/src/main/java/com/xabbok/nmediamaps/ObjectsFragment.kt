package com.xabbok.nmediamaps

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.xabbok.nmediamaps.databinding.FragmentObjectsBinding
import com.xabbok.nmediamaps.presentation.viewmodels.ObjectsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ObjectsFragment : Fragment(R.layout.fragment_objects) {
    private val binding: FragmentObjectsBinding by viewBinding(FragmentObjectsBinding::bind)

    private val viewModel: ObjectsViewModel by activityViewModels()
    private lateinit var adapter: ArrayAdapter<String>
    private val data = ArrayList<String>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_list_item_1,
            data
        )
        binding.objectList.adapter = adapter

        setupSubscribe()
    }

    private fun setupSubscribe() {
        viewModel.data.observe(viewLifecycleOwner) {list ->
            data.clear()
            data.addAll(list.map {
                it.title
            })

            adapter.notifyDataSetChanged()
        }
    }
}