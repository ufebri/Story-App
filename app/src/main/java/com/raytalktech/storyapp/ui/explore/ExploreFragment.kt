package com.raytalktech.storyapp.ui.explore

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
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
import com.raytalktech.storyapp.utils.checkAndRequestPermission
import com.raytalktech.storyapp.utils.showAlert

class ExploreFragment : Fragment() {

    private lateinit var _binding: FragmentExploreBinding
    private val binding get() = _binding
    private lateinit var viewModel: ExploreViewModel
    private val boundsBuilder = LatLngBounds.Builder()
    private var isGranted: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        checkAndRequestPermission(Manifest.permission.ACCESS_COARSE_LOCATION, 12,
            callback = { isGranted ->
                this.isGranted = isGranted
            })

        _binding = FragmentExploreBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (activity != null) {
            val factory = ViewModelFactory.getInstance(requireActivity())
            viewModel = ViewModelProvider(this, factory)[ExploreViewModel::class.java]

            binding.root.isGone = true

            if (isGranted) {
                binding.root.isVisible = true
                val mapFragment =
                    childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
                mapFragment?.getMapAsync(callback)
            } else {
                showAlert(
                    getString(R.string.rationale_title_permission),
                    getString(R.string.rationale_message_location),
                    getString(R.string.text_general_ok),
                    getString(R.string.text_general_cancel),
                    positiveAction = {
                        requireActivity().startActivity(Intent().apply {
                            action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                            data =
                                Uri.fromParts("package", requireContext().packageName, null)
                        })
                    },
                    negativeAction = { findNavController().navigate(R.id.navigation_home) }
                )
            }
        }
    }

    private val callback = OnMapReadyCallback { googleMap -> setupLocation(googleMap) }

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
                            googleMap.addMarker(
                                MarkerOptions().position(location).title(mData.name)
                                    .snippet(mData.description)
                            )
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