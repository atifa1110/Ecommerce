package com.example.ecommerce.main.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.example.ecommerce.api.method.ApiService
import com.example.ecommerce.api.model.ProductPagingFilter
import com.example.ecommerce.api.model.ProductPagingSearch
import com.example.ecommerce.api.response.ProductResponse
import com.example.ecommerce.api.response.SearchResponse
import retrofit2.Response
import javax.inject.Inject

class ProductRepository @Inject constructor(
    private val service: ApiService,
) {
    fun getProduct(search: String) = Pager(PagingConfig(pageSize = 10),
        pagingSourceFactory = {
            ProductPagingSearch(service,search) }
        ).flow

    fun getProductFilter(search:String, brand:String,lowest:Int,highest:Int, sort:String) =
        Pager(PagingConfig(pageSize = 10),
            pagingSourceFactory = {
                ProductPagingFilter(service, search, brand, lowest, highest, sort)
            }
        ).flow

    suspend fun getProductSearch(
        authorization: String,
        search: String,
        limit: Int,
        page: Int
    ): Response<ProductResponse> {
        return service.getProductSearch(authorization,search,limit,page)
    }


    suspend fun searchProductList(
        authorization: String,
        query: String
    ): Response<SearchResponse> {
        return service.searchProductList(authorization,query)
    }

}