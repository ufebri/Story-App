package com.raytalktech.storyapp

import com.raytalktech.storyapp.model.StoriesResult

object DataDummy {

    fun generateDummyQuoteResponse(): List<StoriesResult> {
        val items: MutableList<StoriesResult> = arrayListOf()
        for (i in 0..100) {
            val mData = StoriesResult(
                i.toString(),
                "author + $i",
                "quote $i",
                "image_$i",
                "date_$i",
                "$i".toDouble(),
                "$i".toDouble()
            )
            items.add(mData)
        }
        return items
    }
}