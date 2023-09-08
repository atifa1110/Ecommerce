package com.example.ecommerce.di

import android.util.Log
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import com.example.ecommerce.datastore.DataStoreRepositoryImpl
import com.example.ecommerce.main.main.MainViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class HeaderInterceptor @Inject constructor(
    private val repository: DataStoreRepositoryImpl
) : Interceptor{

    override fun intercept(chain: Interceptor.Chain): Response {
        val newRequest = chain.request().newBuilder().addHeader("Authorization",getAccessToken()).build()
        Log.d("Intercept",newRequest.toString())
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