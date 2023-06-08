package com.xabbok.nmediamaps

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.xabbok.nmediamaps.databinding.FragmentObjectsBinding

class ObjectsFragment : Fragment(R.layout.fragment_objects) {
    private val binding: FragmentObjectsBinding by viewBinding(FragmentObjectsBinding::bind)

    private lateinit var adapter: ArrayAdapter<*>
    private val testData = arrayOf("1", "2", "3", "4", "5")

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, testData)
        binding.objectList.adapter = adapter
    }
}