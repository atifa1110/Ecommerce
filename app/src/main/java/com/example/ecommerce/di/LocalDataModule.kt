package com.example.ecommerce.di

import com.example.ecommerce.room.cart.CartDao
import com.example.ecommerce.room.cart.CartLocalDataSource
import com.example.ecommerce.room.cart.CartLocalDataSourceImpl
import com.example.ecommerce.room.favorite.FavoriteDao
import com.example.ecommerce.room.favorite.FavoriteLocalDataSource
import com.example.ecommerce.room.favorite.FavoriteLocalDataSourceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent


@Module
@InstallIn(SingletonComponent::class)
object LocalDataModule {

    @Provides
    fun provideCartLocalDataSource(cartDao: CartDao):
            CartLocalDataSource = CartLocalDataSourceImpl(cartDao)

    @Provides
    fun provideFavoriteLocalDataSource(favoriteDao: FavoriteDao):
            FavoriteLocalDataSource = FavoriteLocalDataSourceImpl(favoriteDao)

}
