package com.raytalktech.storyapp.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.raytalktech.storyapp.databinding.ItemFeedStoriesBinding
import com.raytalktech.storyapp.model.StoriesResult
import com.raytalktech.storyapp.ui.adapter.viewholder.FeedViewHolder

class StoriesFeedAdapter(
    private val list: List<StoriesResult>,
    private val onItemClick: (StoriesResult) -> Unit
) :
    RecyclerView.Adapter<FeedViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeedViewHolder {
        val binding =
            ItemFeedStoriesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FeedViewHolder(binding, onItemClick)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: FeedViewHolder, position: Int) =
        itemCount.let { holder.bind(list[position]) }

}