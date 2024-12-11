package com.example.core.di

import android.util.Log
import com.example.core.datastore.DataStoreRepositoryImpl
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class HeaderInterceptor @Inject constructor(
    private val repository: DataStoreRepositoryImpl
) : Interceptor{

    override fun intercept(chain: Interceptor.Chain): Response {
        var newRequest = chain.request().newBuilder().addHeader("Authorization", getAccessToken()).build()
        if (newRequest.headers("API_KEY").isNotEmpty()) {
            newRequest = chain.request().newBuilder().addHeader("Authorization", "").build()
        }
        Log.d("Intercept", newRequest.toString())
        Log.d("Intercept", newRequest.header("API_KEY")?.isNotEmpty().toString())
        return chain.proceed(newRequest)
    }

    private fun getAccessToken () : String{
        var token = ""
        runBlocking{
            token = "Bearer ${repository.getAccessToken().first()}"
        }
        return token
    }

}