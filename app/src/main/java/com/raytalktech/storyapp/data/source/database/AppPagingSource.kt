package com.raytalktech.storyapp.data.source.database

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.raytalktech.storyapp.model.StoriesResult
import com.raytalktech.storyapp.network.ApiService
import com.raytalktech.storyapp.utils.Constants.token

class AppPagingSource(private val apiService: ApiService) : PagingSource<Int, StoriesResult>() {

    private companion object {
        const val INITIAL_PAGE_INDEX = 1
    }

    override fun getRefreshKey(state: PagingState<Int, StoriesResult>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, StoriesResult> {
        return try {
            val position = params.key ?: INITIAL_PAGE_INDEX
            val responseData =
                apiService.allFeeds("bearer $token", page = 5, size = params.loadSize, 0)

            LoadResult.Page(
                data = responseData.storiesResult!!,
                prevKey = if (position == INITIAL_PAGE_INDEX) null else position - 1,
                nextKey = if (responseData.storiesResult!!.isNullOrEmpty()) null else position + 1
            )
        } catch (exception: Exception) {
            return LoadResult.Error(exception)
        }
    }
}