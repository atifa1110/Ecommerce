package com.example.ecommerce.test

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.example.core.api.method.ApiService
import com.example.core.api.model.ProductPagingFilter
import com.example.core.api.response.DetailResponse
import com.example.core.api.response.ReviewResponse
import com.example.core.api.response.SearchResponse
import retrofit2.Response
import javax.inject.Inject

class ProductRepository @Inject constructor(
    private val service: ApiService,
) {
    fun getProductFilter(
        search: String?,
        brand: String?,
        lowest: Int?,
        highest: Int?,
        sort: String?
    ) =
        Pager(
            PagingConfig(pageSize = 10, initialLoadSize = 10, prefetchDistance = 1),
            pagingSourceFactory = {
                ProductPagingFilter(service, search, brand, lowest, highest, sort)
            }
        ).flow

    suspend fun searchProductList(
        query: String
    ): Response<SearchResponse> {
        return service.searchProductList(query)
    }

    suspend fun getProductDetail(
        id: String
    ): Response<DetailResponse> {
        return service.getProductDetail(id)
    }

    suspend fun getProductReview(
        id: String
    ): Response<ReviewResponse> {
        return service.getProductReview(id)
    }
}
