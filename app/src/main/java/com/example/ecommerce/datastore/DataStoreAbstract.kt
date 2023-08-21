package com.example.ecommerce.datastore

import kotlinx.coroutines.flow.Flow

interface DataStoreAbstract {
    suspend fun saveOnBoardingState (complete:Boolean)

    suspend fun getOnBoardingState() : Flow<Boolean>
}