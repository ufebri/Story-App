package com.raytalktech.storyapp.ui.home

import android.Manifest
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.raytalktech.storyapp.data.source.remote.ApiResponse
import com.raytalktech.storyapp.data.source.remote.StatusResponse
import com.raytalktech.storyapp.databinding.BottomsheetDetailStoriesBinding
import com.raytalktech.storyapp.databinding.FragmentHomeBinding
import com.raytalktech.storyapp.model.DataResponse
import com.raytalktech.storyapp.model.StoriesResult
import com.raytalktech.storyapp.model.UserModel
import com.raytalktech.storyapp.ui.adapter.StoriesFeedAdapter
import com.raytalktech.storyapp.utils.*

class HomeFragment : Fragment() {

    private lateinit var _binding: FragmentHomeBinding
    private val binding get() = _binding
    private lateinit var viewModel: HomeViewModel
    private lateinit var token: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (activity != null) {

            val factory = ViewModelFactory.getInstance(requireActivity())
            viewModel = ViewModelProvider(this, factory)[HomeViewModel::class.java]

            viewModel.getUser()
                .observe(viewLifecycleOwner, goTo)
        }
    }

    private val goTo = Observer<UserModel> { user ->
        user.let {
            token = it.token
            showTheMainView()
        }
    }

    private fun showTheMainView() {
        if (checkPermission(Manifest.permission.ACCESS_COARSE_LOCATION)) {
            viewModel.getStoriesData(token).observe(viewLifecycleOwner, storiesList)
        } else {
            requestPermission(Manifest.permission.ACCESS_COARSE_LOCATION, 12) { isGranted ->
                if (isGranted) {
                    viewModel.getStoriesData(token).observe(viewLifecycleOwner, storiesList)
                } else {
                    Toast.makeText(requireActivity(), "kenapa dah", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private val storiesList = Observer<ApiResponse<DataResponse>> { result ->
        when (result.status) {
            StatusResponse.EMPTY -> {
                Toast.makeText(requireActivity(), "kenapa dah kosong", Toast.LENGTH_LONG).show()
            }
            StatusResponse.ERROR -> {
                Toast.makeText(requireActivity(), "kenapa dah error", Toast.LENGTH_LONG).show()
            }
            StatusResponse.SUCCESS -> {
                binding.rvMain.apply {
                    layoutManager = LinearLayoutManager(context)
                    val mData: List<StoriesResult>? = result.body.storiesResult

                    if (mData != null) {
                        adapter = StoriesFeedAdapter(mData) { data ->
                            viewModel.getDetailStoriesData(token, data.id)
                                .observe(requireActivity(), detailStories)
                        }
                    }
                }
            }
        }
    }

    private val detailStories = Observer<ApiResponse<DataResponse>> { result ->
        when (result.status) {
            StatusResponse.EMPTY -> {}
            StatusResponse.ERROR -> {}
            StatusResponse.SUCCESS -> showCustomBottomSheet(
                { inflater -> BottomsheetDetailStoriesBinding.inflate(inflater) },
                {
                    val mData = result.body.detailStories
                    if (mData != null) {
                        Glide.with(requireActivity()).load(mData.photoUrl).into(ivDetailPhoto)

                        tvDetailDescription.text = mData.description
                        tvDetailName.text = mData.name

                        val location = Location("")
                        location.latitude = mData.lat
                        location.longitude = mData.lon

                        tvDetailLocation.text = String.format(
                            "%s - %s",
                            location.getShortName(requireActivity()),
                            mData.createdAt.toHumanReadable()
                        )
                    }
                })
        }
    }
}