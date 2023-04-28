package com.raytalktech.storyapp.ui.home

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import androidx.recyclerview.widget.ListUpdateCallback
import com.raytalktech.storyapp.DataDummy
import com.raytalktech.storyapp.MainDispatcherRule
import com.raytalktech.storyapp.data.source.DataRepository
import com.raytalktech.storyapp.getOrAwaitValue
import com.raytalktech.storyapp.model.StoriesResult
import com.raytalktech.storyapp.ui.adapter.StoriesFeedAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class HomeViewModelTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRules = MainDispatcherRule()

    @Mock
    private lateinit var dataRepository: DataRepository

    @Test
    fun `when Get Quote Should Not Null and Return Data`() = runTest {
        val dummyQuote = DataDummy.generateDummyQuoteResponse()
        val data: PagingData<StoriesResult> = QuotePagingSource.snapshot(dummyQuote)
        val expectedQuote = MutableLiveData<PagingData<StoriesResult>>()
        expectedQuote.value = data
        Mockito.`when`(dataRepository.getAllFeed()).thenReturn(expectedQuote)

        val mainViewModel = HomeViewModel(dataRepository)
        val actualQuote: PagingData<StoriesResult> = mainViewModel.stories.getOrAwaitValue()

        val differ = AsyncPagingDataDiffer(
            diffCallback = StoriesFeedAdapter.DIFF_CALLBACK,
            updateCallback = noopListUpdateCallback,
            workerDispatcher = Dispatchers.Main,
        )
        differ.submitData(actualQuote)

        Assert.assertNotNull(differ.snapshot())
        Assert.assertEquals(dummyQuote.size, differ.snapshot().size)
        Assert.assertEquals(dummyQuote[0].name, differ.snapshot()[0]?.name)
    }

    @Test
    fun `when Get Quote Should Not Null`() = runTest {
        val dummyQuote = DataDummy.generateDummyQuoteResponse()
        val data: PagingData<StoriesResult> = QuotePagingSource.snapshot(dummyQuote)
        val expectedQuote = MutableLiveData<PagingData<StoriesResult>>()
        expectedQuote.value = data

        Mockito.`when`(dataRepository.getAllFeed()).thenReturn(expectedQuote)
        val mainViewModel = HomeViewModel(dataRepository)
        val actualQuote: PagingData<StoriesResult> = mainViewModel.stories.getOrAwaitValue()

        val differ = AsyncPagingDataDiffer(
            diffCallback = StoriesFeedAdapter.DIFF_CALLBACK,
            updateCallback = noopListUpdateCallback,
            workerDispatcher = Dispatchers.Main,
        )
        differ.submitData(actualQuote)

    }

    @Test
    fun `when Get Quote Empty Should Return No Data`() = runTest {
        val data: PagingData<StoriesResult> = PagingData.from(emptyList())
        val expectedQuote = MutableLiveData<PagingData<StoriesResult>>()
        expectedQuote.value = data
        Mockito.`when`(dataRepository.getAllFeed()).thenReturn(expectedQuote)
        val mainViewModel = HomeViewModel(dataRepository)
        val actualQuote: PagingData<StoriesResult> = mainViewModel.stories.getOrAwaitValue()
        val differ = AsyncPagingDataDiffer(
            diffCallback = StoriesFeedAdapter.DIFF_CALLBACK,
            updateCallback = noopListUpdateCallback,
            workerDispatcher = Dispatchers.Main,
        )
        differ.submitData(actualQuote)
        Assert.assertEquals(0, differ.snapshot().size)
    }

}

val noopListUpdateCallback = object : ListUpdateCallback {
    override fun onInserted(position: Int, count: Int) {}
    override fun onRemoved(position: Int, count: Int) {}
    override fun onMoved(fromPosition: Int, toPosition: Int) {}
    override fun onChanged(position: Int, count: Int, payload: Any?) {}
}

class QuotePagingSource : PagingSource<Int, LiveData<List<StoriesResult>>>() {
    companion object {
        fun snapshot(items: List<StoriesResult>): PagingData<StoriesResult> {
            return PagingData.from(items)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, LiveData<List<StoriesResult>>>): Int {
        return 0
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, LiveData<List<StoriesResult>>> {
        return LoadResult.Page(emptyList(), 0, 1)
    }
}