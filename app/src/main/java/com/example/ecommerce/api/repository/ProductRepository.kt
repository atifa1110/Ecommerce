package com.example.ecommerce.api.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.example.ecommerce.api.method.ApiService
import com.example.ecommerce.api.model.ProductPagingFilter
import com.example.ecommerce.api.response.DetailResponse
import com.example.ecommerce.api.response.ReviewResponse
import com.example.ecommerce.api.response.SearchResponse
import retrofit2.Response
import javax.inject.Inject

class ProductRepository @Inject constructor(
    private val service: ApiService,
) {
    fun getProductFilter(search:String?, brand:String?,lowest:Int?,highest:Int?, sort:String?) =
        Pager(PagingConfig(pageSize = 10, initialLoadSize = 10, prefetchDistance = 1),
            pagingSourceFactory = {
                ProductPagingFilter(service, search, brand, lowest, highest, sort)
            }
        ).flow

    suspend fun searchProductList(
        //authorization: String,
        query: String
    ): Response<SearchResponse> {
        return service.searchProductList(query)
    }

    suspend fun getProductDetail(
        //authorization: String,
        id: String
    ): Response<DetailResponse> {
        return service.getProductDetail(id)
    }

    suspend fun getProductReview(
        //authorization: String,
        id: String
    ): Response<ReviewResponse> {
        return service.getProductReview(id)
    }

}