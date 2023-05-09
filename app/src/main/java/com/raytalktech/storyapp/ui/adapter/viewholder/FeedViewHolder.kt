package com.raytalktech.storyapp.ui.adapter.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.raytalktech.storyapp.databinding.ItemFeedStoriesBinding
import com.raytalktech.storyapp.model.StoriesResult

class FeedViewHolder(
    private val binding: ItemFeedStoriesBinding,
    private val onItemClick: (StoriesResult) -> Unit
) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(item: StoriesResult) {
        with(binding) {
            //Set Photo
            Glide.with(itemView.context).load(item.photoUrl).into(ivItemPhoto)

            //Set name
            tvItemName.text = item.name

            //Passing the Data
            itemView.setOnClickListener { onItemClick(item) }
        }
    }
}