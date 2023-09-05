package com.example.ecommerce.di

import android.content.Context
import androidx.room.Room
import com.example.ecommerce.room.CartDao
import com.example.ecommerce.room.CartDatabase
import com.example.ecommerce.room.CartRepository
import com.example.ecommerce.room.CartRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object RoomModule {
    @Provides
    fun provideProductsDao(cartDatabase: CartDatabase)
            : CartDao {
        return cartDatabase.cartDao()
    }

    @Provides
    @Singleton
    fun provideProductsDatabase(@ApplicationContext context: Context)
            : CartDatabase {
        return Room.databaseBuilder(
            context,
            CartDatabase::class.java,
            "CartDatabase"
        ).fallbackToDestructiveMigration().build()
    }
}