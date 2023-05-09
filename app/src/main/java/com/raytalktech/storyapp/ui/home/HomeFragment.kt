package com.raytalktech.storyapp.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.paging.PagingData
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.raytalktech.storyapp.data.source.remote.ApiResponse
import com.raytalktech.storyapp.data.source.remote.StatusResponse
import com.raytalktech.storyapp.databinding.BottomsheetDetailStoriesBinding
import com.raytalktech.storyapp.databinding.FragmentHomeBinding
import com.raytalktech.storyapp.model.DataResponse
import com.raytalktech.storyapp.model.StoriesResult
import com.raytalktech.storyapp.ui.adapter.LoadingStateAdapter
import com.raytalktech.storyapp.ui.adapter.StoriesFeedAdapter
import com.raytalktech.storyapp.utils.ViewModelFactory
import com.raytalktech.storyapp.utils.showCustomBottomSheet
import com.raytalktech.storyapp.utils.toHumanReadable

class HomeFragment : Fragment() {

    private lateinit var _binding: FragmentHomeBinding
    private val binding get() = _binding
    private lateinit var viewModel: HomeViewModel

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

            showTheMainView()
        }
    }

    private fun showTheMainView() {
        binding.rvMain.apply {
            layoutManager = LinearLayoutManager(context)
            val mAdapter = StoriesFeedAdapter(onItemClick = { data ->
                viewModel.getDetailStoriesData(data.id)
                    .observe(requireActivity(), detailStories)
            })

            adapter = mAdapter.withLoadStateFooter(
                footer = LoadingStateAdapter {
                    mAdapter.retry()
                }
            )

            viewModel.stories.observe(
                requireActivity(),
                Observer<PagingData<StoriesResult>> { mData ->
                    mAdapter.submitData(lifecycle, mData)
                })
        }
    }

    private val detailStories = Observer<ApiResponse<DataResponse>> { result ->
        when (result.status) {
            StatusResponse.EMPTY -> {}
            StatusResponse.ERROR -> {}
            StatusResponse.SUCCESS -> showCustomBottomSheet({ inflater ->
                BottomsheetDetailStoriesBinding.inflate(
                    inflater
                )
            }, {
                val mData = result.body.detailStories
                if (mData != null) {
                    Glide.with(requireActivity()).load(mData.photoUrl).into(ivDetailPhoto)

                    tvDetailDescription.text = mData.description
                    tvDetailName.text = mData.name

                    tvDetailLocation.text = mData.createdAt.toHumanReadable()
                }
            })
        }
    }
}