package com.example.core.di

import com.example.core.room.cart.CartDao
import com.example.core.room.cart.CartLocalDataSource
import com.example.core.room.cart.CartLocalDataSourceImpl
import com.example.core.room.favorite.FavoriteDao
import com.example.core.room.favorite.FavoriteLocalDataSource
import com.example.core.room.favorite.FavoriteLocalDataSourceImpl
import com.example.core.room.notification.NotificationDao
import com.example.core.room.notification.NotificationRepository
import com.example.core.room.notification.NotificationRepositoryImpl
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

    @Provides
    fun provideNotificationLocalDataSource(notificationDao: NotificationDao):
            NotificationRepository = NotificationRepositoryImpl(notificationDao)

}
