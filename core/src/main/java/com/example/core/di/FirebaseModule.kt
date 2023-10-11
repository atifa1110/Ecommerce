package com.example.core.di

import com.example.core.api.method.FirebaseService
import com.example.core.firebase.MessagingRepository
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
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
    fun provideFirebaseCloudMessagingApi(factory: GsonConverterFactory): FirebaseService =
        Retrofit.Builder()
            .baseUrl("https://fcm.googleapis.com/tokophincon-59381/")
            .addConverterFactory(factory)
            .build()
            .create(FirebaseService::class.java)

    @Provides
    @Singleton
    fun provideFirebaseCloudMessagingRepository(api: FirebaseService): MessagingRepository =
        MessagingRepository(api)

    @Provides
    @Singleton
    fun provideAnalytics() = Firebase.analytics

}