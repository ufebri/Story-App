package com.raytalktech.storyapp.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.raytalktech.storyapp.databinding.ItemFeedStoriesBinding
import com.raytalktech.storyapp.model.StoriesResult
import com.raytalktech.storyapp.ui.adapter.viewholder.FeedViewHolder

class StoriesFeedAdapter(private val onItemClick: (StoriesResult) -> Unit) :
    PagingDataAdapter<StoriesResult, FeedViewHolder>(DIFF_CALLBACK) {

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<StoriesResult>() {
            override fun areItemsTheSame(oldItem: StoriesResult, newItem: StoriesResult): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: StoriesResult,
                newItem: StoriesResult
            ): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeedViewHolder {
        val binding =
            ItemFeedStoriesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FeedViewHolder(binding, onItemClick)
    }

    override fun onBindViewHolder(holder: FeedViewHolder, position: Int) =
        itemCount.let { holder.bind(getItem(position)!!) }
}