package com.xabbok.nmediamaps.presentation.fragments

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getDrawable
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.collections.MarkerManager
import com.google.maps.android.ktx.awaitAnimateCamera
import com.google.maps.android.ktx.awaitMap
import com.google.maps.android.ktx.model.cameraPosition
import com.google.maps.android.ktx.utils.collection.addMarker
import com.xabbok.nmediamaps.R
import com.xabbok.nmediamaps.databinding.FragmentMapsBinding
import com.xabbok.nmediamaps.extensions.icon
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MapsFragment : Fragment(R.layout.fragment_maps) {
    private lateinit var googleMap: GoogleMap
    private val marketManager: MarkerManager by lazy { MarkerManager(googleMap) }
    private val binding: FragmentMapsBinding by viewBinding(FragmentMapsBinding::bind)
    private val mapFragment: SupportMapFragment by lazy { childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment }

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(state = Lifecycle.State.RESUMED) {
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
                                Toast.makeText(
                                    requireContext(),
                                    "Last location: $it",
                                    Toast.LENGTH_LONG
                                )
                                    .show()
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

                val target = LatLng(55.751999, 37.617734)
                googleMap.awaitAnimateCamera(
                    CameraUpdateFactory.newCameraPosition(
                        cameraPosition {
                            target(target)
                            zoom(15F)
                        }
                    )
                )

                val collection: MarkerManager.Collection = marketManager.newCollection().apply {
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
                }
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