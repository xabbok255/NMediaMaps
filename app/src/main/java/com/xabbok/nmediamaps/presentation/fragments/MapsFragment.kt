package com.xabbok.nmediamaps.presentation.fragments

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.collections.MarkerManager
import com.google.maps.android.ktx.awaitAnimateCamera
import com.google.maps.android.ktx.awaitMap
import com.google.maps.android.ktx.model.cameraPosition
import com.google.maps.android.ktx.utils.collection.addMarker
import com.xabbok.nmediamaps.R
import com.xabbok.nmediamaps.adapter.MapInfoWindowAdapter
import com.xabbok.nmediamaps.databinding.FragmentMapsBinding
import com.xabbok.nmediamaps.dto.GeoObject
import com.xabbok.nmediamaps.extensions.icon
import com.xabbok.nmediamaps.presentation.viewmodels.ObjectsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MapsFragment : Fragment(R.layout.fragment_maps) {
    private val viewModel: ObjectsViewModel by activityViewModels()
    private lateinit var googleMap: GoogleMap
    private val markerManager: MarkerManager by lazy { MarkerManager(googleMap) }
    private val binding: FragmentMapsBinding by viewBinding(FragmentMapsBinding::bind)
    private val mapFragment: SupportMapFragment by lazy { childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment }

    private val markerManagerCurrentSelectedCollection: MarkerManager.Collection by lazy {
        markerManager.newCollection(
            "CurrentSelection"
        )
    }

    private val markerManagerObjectListCollection: MarkerManager.Collection by lazy {
        markerManager.newCollection(
            "ObjectList"
        )
    }

    //private val markerManagerObjectsCollection: MarkerManager

    @SuppressLint("MissingPermission")
    val requestPermissionsLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                googleMap.apply {
                    isMyLocationEnabled = true
                    uiSettings.isMyLocationButtonEnabled = true
                }
            } else {

            }
        }

    @SuppressLint("PotentialBehaviorOverride")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewLifecycleOwner.lifecycleScope.launch {

            googleMap = mapFragment.awaitMap()

            when {
                ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED -> {
                    googleMap.apply {
                        isMyLocationEnabled = true
                        uiSettings.isMyLocationButtonEnabled = true
                    }

                    LocationServices.getFusedLocationProviderClient(requireContext())
                        .lastLocation.addOnSuccessListener {
                            /*Toast.makeText(
                                requireContext(),
                                "Last location: $it",
                                Toast.LENGTH_LONG
                            )
                                .show()*/
                        }
                }

                shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION) -> {
                    Toast.makeText(requireContext(), "need GPS !!!", Toast.LENGTH_LONG)
                        .show()
                }

                else -> requestPermissionsLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
            }

            googleMap.apply {
                isTrafficEnabled = true
                isBuildingsEnabled = true
                uiSettings.apply {
                    isZoomControlsEnabled = true
                    setAllGesturesEnabled(true)
                }
            }

            viewModel.firstMoveCameraCurrentPosition.value = true

            viewModel.firstMoveCameraCurrentPosition.observe(viewLifecycleOwner) {
                LocationServices.getFusedLocationProviderClient(requireContext())
                    .lastLocation.addOnSuccessListener { location ->
                        location?.let {
                            viewLifecycleOwner.lifecycleScope.launch {
                                googleMap.awaitAnimateCamera(
                                    CameraUpdateFactory.newCameraPosition(
                                        cameraPosition {
                                            target(LatLng(it.latitude, it.longitude))
                                            zoom(15F)
                                        }
                                    ), 500
                                )
                            }
                        }
                    }
            }

            lifecycleScope.launch {
                viewModel.moveMapPosition.collect() {
                    it?.let {
                        lifecycleScope.launch {
                            googleMap.awaitAnimateCamera(
                                CameraUpdateFactory.newCameraPosition(
                                    cameraPosition {
                                        target(LatLng(it.lat, it.long))
                                        zoom(15F)
                                    }
                                ), 500
                            )
                        }
                    }
                }
            }

            binding.lastTouchedPointAddButton.setOnClickListener {
                markerManagerCurrentSelectedCollection.markers.toList().singleOrNull()?.let {
                    val geoObject = GeoObject(
                        lat = it.position.latitude,
                        long = it.position.longitude,
                        title = ""
                    )
                    viewModel.lastTouchedPoint.postValue(null)
                    findNavController().navigate(
                        R.id.action_global_objectEditFragment,
                        bundleOf(Pair(INTENT_EXTRA_OBJECT, geoObject))
                    )
                }

            }

            viewModel.lastTouchedPoint.observe(viewLifecycleOwner) { marker ->
                markerManagerCurrentSelectedCollection.apply {
                    clear()

                    binding.lastTouchedPointAddButton.isVisible = marker != null

                    marker?.let {
                        addMarker {
                            position(it)
                            icon(AppCompatResources.getDrawable(requireContext(), R.drawable.add_location_icon)!!)
                        }
                    }
                }
            }

            viewModel.data.observe(viewLifecycleOwner) { list ->
                markerManagerObjectListCollection.apply {
                    clear()
                    list?.forEach {
                        addMarker(
                            MarkerOptions().apply {
                                position(LatLng(it.lat, it.long))
                                title(it.title)
                            }
                        ).apply {
                            tag = it
                        }
                    }
                }
            }

            /*val collection: MarkerManager.Collection = markerManager.newCollection().apply {
                addMarker {
                    position(target)

                    icon(
                        requireNotNull(
                            getDrawable(
                                requireContext(),
                                R.drawable.baseline_question_mark_24
                            )
                        )
                    )
                    title("12345")
                }.apply {
                    tag = "Data 123"
                }
            }

            collection.setOnMarkerClickListener { marker ->
                Toast.makeText(
                    requireContext(),
                    (marker.tag as String),
                    Toast.LENGTH_LONG
                ).show()
                true
            }*/

            markerManagerObjectListCollection.setInfoWindowAdapter(
                MapInfoWindowAdapter(
                    requireContext()
                )
            )

            googleMap.setOnMapClickListener {
                viewModel.lastTouchedPoint.postValue(it)
            }

        }
    }

    override fun onResume() {
        super.onResume()
        mapFragment.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapFragment.onPause()
    }

    override fun onDestroyView() {
        super.onDestroyView()

        mapFragment.onDestroyView()
    }
}