package com.example.ecommerce.di

import android.content.Context
import androidx.room.Room
import com.example.ecommerce.room.cart.CartDao
import com.example.ecommerce.room.cart.CartDatabase
import com.example.ecommerce.room.favorite.FavoriteDao
import com.example.ecommerce.room.notification.NotificationDao
import com.example.ecommerce.room.notification.NotificationDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

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

    @Provides
    fun provideProductsDao(cartDatabase: CartDatabase)
            : CartDao {
        return cartDatabase.cartDao()
    }

    @Provides
    fun provideFavoriteDao(cartDatabase: CartDatabase)
            : FavoriteDao {
        return cartDatabase.FavoriteDao()
    }

    @Provides
    @Singleton
    fun provideNotificationDatabase(@ApplicationContext context: Context)
            : NotificationDatabase {
        return Room.databaseBuilder(
            context,
            NotificationDatabase::class.java,
            "NotificationDatabase"
        ).fallbackToDestructiveMigration().build()
    }

    @Provides
    fun provideNotificationDao(notificationDatabase: NotificationDatabase)
            : NotificationDao{
        return notificationDatabase.notificationDao()
    }
}

