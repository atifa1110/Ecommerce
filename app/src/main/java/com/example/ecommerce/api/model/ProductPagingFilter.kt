package com.example.ecommerce.api.model

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.ecommerce.api.method.ApiService
import com.example.ecommerce.util.Constant
import kotlinx.coroutines.delay

class ProductPagingFilter (
    private val service: ApiService,
    private val search : String?,
    private val brand : String?,
    private val lowest : Int?,
    private val highest : Int?,
    private val sort : String?
): PagingSource<Int, Product>() {
    override fun getRefreshKey(state: PagingState<Int, Product>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Product> {
        val page = params.key ?: 1
        val response = service.getProductFilter(search, brand, lowest, highest, sort, 10, page)

        return try {
            //delay(7000)
            LoadResult.Page(
                data = response.body()!!.data.items,
                prevKey = null,
                nextKey = if (page==response.body()!!.data.totalPages) null else page.plus(1)
            )
        } catch (error: Exception) {
            return LoadResult.Error(Throwable(response.errorBody().toString()))
        }
    }


}