package com.example.core.api.method

import com.example.core.api.model.RefreshToken
import com.example.core.api.request.AuthRequest
import com.example.core.api.response.DetailResponse
import com.example.core.api.response.LoginResponse
import com.example.core.api.response.ProductResponse
import com.example.core.api.response.ProfileResponse
import com.example.core.api.response.RefreshResponse
import com.example.core.api.response.RegisterResponse
import com.example.core.api.response.ReviewResponse
import com.example.core.api.response.SearchResponse
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
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
    suspend fun refreshToken(
        @Header("API_KEY") api: String,
        @Body token: RefreshToken
    ): Response<RefreshResponse>

    @Multipart
    @POST("profile")
    suspend fun profileUser (
        @Part userImage : MultipartBody.Part,
        @Part userName : MultipartBody.Part
    ) : Response<ProfileResponse>

    @POST("products")
    suspend fun getProductFilter(
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
        @Query("query") query: String
    ) : Response<SearchResponse>

    @GET("products/{id}")
    suspend fun getProductDetail(
        @Path("id") id: String
    ) : Response<DetailResponse>

    @GET("review/{id}")
    suspend fun getProductReview(
         @Path("id") id: String
    ) : Response<ReviewResponse>
}