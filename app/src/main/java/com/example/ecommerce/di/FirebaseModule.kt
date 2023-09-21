package com.example.ecommerce.di

import com.example.ecommerce.api.method.FirebaseMessagingApi
import com.example.ecommerce.firebase.FirebaseMessagingRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object FirebaseModule {

    @Provides
    @Singleton
    fun provideGsonConverterFactory(): GsonConverterFactory = GsonConverterFactory.create()

    @Provides
    @Singleton
    fun provideFirebaseCloudMessagingApi(factory: GsonConverterFactory): FirebaseMessagingApi =
        Retrofit.Builder()
            .baseUrl("https://fcm.googleapis.com/tokophincon-59381/")
            .addConverterFactory(factory)
            .build()
            .create(FirebaseMessagingApi::class.java)

    @Provides
    @Singleton
    fun provideFirebaseCloudMessagingRepository(api: FirebaseMessagingApi): FirebaseMessagingRepository =
        FirebaseMessagingRepository(api)

}