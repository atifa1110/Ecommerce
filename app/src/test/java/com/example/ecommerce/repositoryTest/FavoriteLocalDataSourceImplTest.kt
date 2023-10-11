package com.example.ecommerce.repositoryTest

import com.example.core.room.favorite.Favorite
import com.example.core.room.favorite.FavoriteDao
import com.example.core.room.favorite.FavoriteLocalDataSourceImpl
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class FavoriteLocalDataSourceImplTest {

    private lateinit var favoriteDao: FavoriteDao
    private lateinit var favoriteLocalDataSourceImpl: FavoriteLocalDataSourceImpl

    @Before
    fun setup() {
        favoriteDao = mock()
        favoriteLocalDataSourceImpl = FavoriteLocalDataSourceImpl(favoriteDao)
    }

    @Test
    fun `test add to favorite repository`() {
        runBlocking {
            whenever(
                favoriteDao.addToFavorite(
                    Favorite(
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
                )
            ).thenReturn(Unit)

            val result = favoriteLocalDataSourceImpl.addToFavorite(
                Favorite(
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
            )

            assertEquals(Unit, result)
        }
    }

    @Test
    fun `test update favorite repository`() {
        runBlocking {
            whenever(
                favoriteDao.updateFavorite("1", true)
            ).thenReturn(Unit)

            val result = favoriteLocalDataSourceImpl.updateFavorite("1", true)

            assertEquals(Unit, result)
        }
    }

    @Test
    fun `test delete favorite repository`() {
        runBlocking {
            whenever(
                favoriteDao.deleteFavoriteById("1")
            ).thenReturn(Unit)

            val result = favoriteLocalDataSourceImpl.deleteFavoriteById("1")

            assertEquals(Unit, result)
        }
    }

    @Test
    fun `test get all favorite repository`() {
        runBlocking {
            val expected = flowOf(
                listOf(
                    Favorite(
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
                )
            )

            whenever(
                favoriteDao.getAllFavorite()
            ).thenReturn(expected)

            val result = favoriteLocalDataSourceImpl.getAllFavorite()

            assertEquals(expected, result)
        }
    }

    @Test
    fun `test get favorite id repository`() {
        runBlocking {
            val expected = listOf(
                Favorite(
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
            )

            whenever(
                favoriteDao.getFavoriteById("b774d021-250a-4c3a-9c58-ab39edb36de5")
            ).thenReturn(expected)

            val result =
                favoriteLocalDataSourceImpl.getFavoriteById("b774d021-250a-4c3a-9c58-ab39edb36de5")

            assertEquals(expected, result)
        }
    }
}
