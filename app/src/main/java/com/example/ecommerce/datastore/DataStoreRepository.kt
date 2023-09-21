package com.example.ecommerce.datastore

import kotlinx.coroutines.flow.Flow

interface DataStoreRepository {

    suspend fun saveOnBoardingState (complete:Boolean)
    fun getOnBoardingState() : Flow<Boolean>

    suspend fun saveHasLoginState (complete:Boolean)
    fun getLoginState() : Flow<Boolean>

    suspend fun saveProfileName (name:String)
    fun getProfileName() : Flow<String>

    suspend fun saveAccessToken (token:String)
    fun getAccessToken() : Flow<String>

    suspend fun saveTokenMessaging(token: String)
    fun getTokenMessaging() : Flow<String>
}