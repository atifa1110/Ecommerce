package com.example.ecommerce.databaseTest

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.core.room.cart.CartDatabase
import com.example.core.room.favorite.Favorite
import com.example.core.room.favorite.FavoriteDao
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class FavoriteDatabaseTest {

    private lateinit var database: CartDatabase
    private lateinit var dao: FavoriteDao

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database =
            Room.inMemoryDatabaseBuilder(context, CartDatabase::class.java).allowMainThreadQueries()
                .build()
        dao = database.FavoriteDao()
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun `test add to favorite room`() {
        runBlocking {
            val favorite = Favorite(
                "b774d021-250a-4c3a-9c58-ab39edb36de5",
                "Dell",
                13849000,
                "image1",
                "Dell",
                "DELL",
                "DellStore",
                13,
                44,
                10,
                5,
                100,
                5.0F,
                "RAM 16GB",
                0,
                1,
                false
            )

            dao.addToFavorite(favorite)
            val retrieve = dao.getAllFavorite().first()
            assertEquals(retrieve.isNotEmpty(), true)
        }
    }

    @Test
    fun `test delete favorite room`() {
        runBlocking {
            val favorite = Favorite(
                "b774d021-250a-4c3a-9c58-ab39edb36de5",
                "Dell",
                13849000,
                "image1",
                "Dell",
                "DELL",
                "DellStore",
                13,
                44,
                10,
                5,
                100,
                5.0F,
                "RAM 16GB",
                0,
                1,
                false
            )

            dao.addToFavorite(favorite)
            dao.deleteFavoriteById("b774d021-250a-4c3a-9c58-ab39edb36de5")

            val retrieve = dao.getAllFavorite().first()
            assertEquals(retrieve.isEmpty(), true)
        }
    }

    @Test
    fun `test get all favorite room`() {
        runBlocking {
            val favorite = Favorite(
                "b774d021-250a-4c3a-9c58-ab39edb36de5",
                "Dell",
                13849000,
                "image1",
                "Dell",
                "DELL",
                "DellStore",
                13,
                44,
                10,
                5,
                100,
                5.0F,
                "RAM 16GB",
                0,
                1,
                false
            )

            dao.addToFavorite(favorite)
            val retrieve = dao.getAllFavorite().first()
            assertEquals(retrieve.isNotEmpty(), true)
        }
    }

    @Test
    fun `test get favorite id room`() {
        runBlocking {
            val favorite = Favorite(
                "b774d021-250a-4c3a-9c58-ab39edb36de5",
                "Dell",
                13849000,
                "image1",
                "Dell",
                "DELL",
                "DellStore",
                13,
                44,
                10,
                5,
                100,
                5.0F,
                "RAM 16GB",
                0,
                1,
                false
            )

            dao.addToFavorite(favorite)
            dao.getFavoriteById("b774d021-250a-4c3a-9c58-ab39edb36de5")

            val retrieve = dao.getAllFavorite().first()
            assertEquals(retrieve.isNotEmpty(), true)
        }
    }

    @Test
    fun `test update favorite room`() {
        runBlocking {
            val favorite = Favorite(
                "b774d021-250a-4c3a-9c58-ab39edb36de5",
                "Dell",
                13849000,
                "image1",
                "Dell",
                "DELL",
                "DellStore",
                13,
                44,
                10,
                5,
                100,
                5.0F,
                "RAM 16GB",
                0,
                1,
                false
            )

            dao.addToFavorite(favorite)
            dao.updateFavorite("b774d021-250a-4c3a-9c58-ab39edb36de5", true)
            val retrieve = dao.getAllFavorite().first()
            assertEquals(retrieve.any { it.favorite == true }, true)
        }
    }
}
