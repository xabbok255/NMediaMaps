package com.xabbok.nmediamaps

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.xabbok.nmediamaps.databinding.FragmentObjectEditBinding
import com.xabbok.nmediamaps.presentation.viewmodels.ObjectsViewModel

const val INTENT_EXTRA_OBJECT = "OBJECT-DATA"

class ObjectEditFragment : Fragment(R.layout.fragment_object_edit) {
    private val binding: FragmentObjectEditBinding by viewBinding(FragmentObjectEditBinding::bind)
    private val viewModel: ObjectsViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.lifecycleOwner = viewLifecycleOwner

        @Suppress("DEPRECATION")
        binding.data = arguments?.getParcelable(
            INTENT_EXTRA_OBJECT
        )

        setupListeners()
    }

    private fun setupListeners() {
        binding.submitButton.setOnClickListener {
            binding.data?.let {
                viewModel.save(it)
            }
        }

        binding.cancelButton.setOnClickListener {
            findNavController().navigateUp()
        }
    }
}