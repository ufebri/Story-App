package com.raytalktech.storyapp.ui.adapter.viewholder

import android.location.Location
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.raytalktech.storyapp.databinding.ItemFeedStoriesBinding
import com.raytalktech.storyapp.model.StoriesResult
import com.raytalktech.storyapp.utils.getShortName

class FeedViewHolder(
    private val binding: ItemFeedStoriesBinding,
    private val onItemClick: (StoriesResult) -> Unit
) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(item: StoriesResult) {
        with(binding) {
            //Set Photo
            Glide.with(itemView.context).load(item.photoUrl).into(ivItemPhoto)

            //Set Name of User & they Location
            val location = Location("")
            location.latitude = item.lat
            location.longitude = item.lon

            tvItemName.text =
                String.format("%s\n%s", item.name, location.getShortName(itemView.context))

            //Passing the Data
            itemView.setOnClickListener { onItemClick(item) }
        }
    }
}