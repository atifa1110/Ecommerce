package com.example.ecommerce.api.method

import com.example.ecommerce.api.request.AuthRequest
import com.example.ecommerce.api.response.DetailResponse
import com.example.ecommerce.api.response.LoginResponse
import com.example.ecommerce.api.response.ProductResponse
import com.example.ecommerce.api.response.ProfileResponse
import com.example.ecommerce.api.response.RegisterResponse
import com.example.ecommerce.api.response.ReviewResponse
import com.example.ecommerce.api.response.SearchResponse
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @POST("login")
    suspend fun loginUser(
        @Header("API_KEY") api : String ,
        @Body loginRequest: AuthRequest
    ): Response<LoginResponse>

    @POST("register")
    suspend fun registerUser(
        @Header("API_KEY") api : String ,
        @Body registerRequest: AuthRequest
    ): Response<RegisterResponse>

    @POST("refresh")
    suspend fun refreshToken (
        @Header("API_KEY") api : String ,
        @Query("token") token : String
    ) : Response<ResponseBody>

    @POST("profile")
    suspend fun profileUser (
        @Header("Authorization") authorization : String,
        @Query("userName") userName : String,
        @Query("userImage") userImage : String
    ) : Response<ProfileResponse>

    @POST("products")
    suspend fun getProductFilter(
        //@Header("Authorization") authorization: String,
        @Query("search") search: String?,
        @Query("brand") brand:String?,
        @Query("lowest") lowestPrice : Int?,
        @Query("highest") highestPrice : Int?,
        @Query("sort") sort: String?,
        @Query("limit") limit: Int,
        @Query("page") page:Int
    ) : Response<ProductResponse>

    @POST("search")
    suspend fun searchProductList(
        //@Header("Authorization") authorization: String,
        @Query("query") query: String
    ) : Response<SearchResponse>

    @GET("products/{id}")
    suspend fun getProductDetail(
        //@Header("Authorization") authorization: String,
        @Path("id") id: String
    ) : Response<DetailResponse>

    @GET("review/{id}")
    suspend fun getProductReview(
        //@Header("Authorization") authorization: String,
        @Path("id") id: String
    ) : Response<ReviewResponse>
}