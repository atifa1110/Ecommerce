package com.example.ecommerce.di

import com.example.ecommerce.room.cart.CartLocalDataSource
import com.example.ecommerce.room.cart.CartRepository
import com.example.ecommerce.room.cart.CartRepositoryImpl
import com.example.ecommerce.room.favorite.FavoriteLocalDataSource
import com.example.ecommerce.room.favorite.FavoriteRepository
import com.example.ecommerce.room.favorite.FavoriteRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

//    @Singleton
//    @Binds
//    abstract fun providesProductsRepository(cartRepositoryImpl: CartRepositoryImpl
//    ): CartRepository

    @Provides
    fun provideCartRepository(
        cartLocalDataSource: CartLocalDataSource
    ): CartRepository = CartRepositoryImpl(cartLocalDataSource)


    @Provides
    fun provideFavoriteRepository(
        favoriteLocalDataSource: FavoriteLocalDataSource
    ): FavoriteRepository = FavoriteRepositoryImpl(favoriteLocalDataSource)

}