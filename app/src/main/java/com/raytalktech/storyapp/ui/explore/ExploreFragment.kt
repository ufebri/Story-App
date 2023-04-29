package com.raytalktech.storyapp.ui.explore

import android.Manifest
import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import com.raytalktech.storyapp.R
import com.raytalktech.storyapp.data.source.remote.ApiResponse
import com.raytalktech.storyapp.data.source.remote.StatusResponse
import com.raytalktech.storyapp.databinding.FragmentExploreBinding
import com.raytalktech.storyapp.model.DataResponse
import com.raytalktech.storyapp.utils.ViewModelFactory
import com.raytalktech.storyapp.utils.checkPermission
import com.raytalktech.storyapp.utils.requestPermission

class ExploreFragment : Fragment() {

    private lateinit var _binding: FragmentExploreBinding
    private val binding get() = _binding
    private lateinit var viewModel: ExploreViewModel
    private val boundsBuilder = LatLngBounds.Builder()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentExploreBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (activity != null) {
            val factory = ViewModelFactory.getInstance(requireActivity())
            viewModel = ViewModelProvider(this, factory)[ExploreViewModel::class.java]

            val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
            mapFragment?.getMapAsync(callback)
        }
    }

    @SuppressLint("MissingPermission")
    private val callback = OnMapReadyCallback { googleMap ->
        if (checkPermission(Manifest.permission.ACCESS_COARSE_LOCATION)) {
            setupLocation(googleMap)
        } else {
            requestPermission(Manifest.permission.ACCESS_COARSE_LOCATION, 12) { isGranted ->
                if (isGranted) {
                    setupLocation(googleMap)
                } else {
                    Toast.makeText(requireActivity(), "kenapa dah", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun setupLocation(googleMap: GoogleMap) {
        viewModel.getStoriesData()
            .observe(viewLifecycleOwner, Observer<ApiResponse<DataResponse>> { result ->
                when (result.status) {
                    StatusResponse.EMPTY -> {
                        Toast.makeText(requireActivity(), "kenapa dah kosong", Toast.LENGTH_LONG)
                            .show()
                    }
                    StatusResponse.ERROR -> {
                        Toast.makeText(requireActivity(), "kenapa dah error", Toast.LENGTH_LONG)
                            .show()
                    }
                    StatusResponse.SUCCESS -> {

                        googleMap.isMyLocationEnabled = true

                        result.body.storiesResult?.forEach { mData ->
                            val location = LatLng(mData.lat, mData.lon)
                            googleMap.addMarker(MarkerOptions().position(location).title(mData.name).snippet(mData.description))
                            boundsBuilder.include(location)
                        }

                        val bounds: LatLngBounds = boundsBuilder.build()
                        googleMap.animateCamera(
                            CameraUpdateFactory.newLatLngBounds(
                                bounds,
                                resources.displayMetrics.widthPixels,
                                resources.displayMetrics.heightPixels,
                                300
                            )
                        )
                    }
                }
            })
    }
}