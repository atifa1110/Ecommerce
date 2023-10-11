package com.example.ecommerce.databaseTest

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.core.room.cart.Cart
import com.example.core.room.cart.CartDao
import com.example.core.room.cart.CartDatabase
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class CartDatabaseTest {

    private lateinit var database: CartDatabase
    private lateinit var dao: CartDao

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database =
            Room.inMemoryDatabaseBuilder(context, CartDatabase::class.java).allowMainThreadQueries()
                .build()
        dao = database.cartDao()
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun `test add to cart room`() {
        runBlocking {
            val entity = Cart(
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
            dao.addToCart(entity)

            // Retrieve get all data
            val retrievedEntity = dao.getAllCart().first()
            assertEquals(retrievedEntity.isNotEmpty(), true)
        }
    }

    @Test
    fun `test find id room`() {
        runBlocking {
            val entity = Cart(
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
            dao.addToCart(entity)
            // dao.deleteById("b774d021-250a-4c3a-9c58-ab39edb36de5")
            // Retrieve get all data
            val retrievedEntity = dao.findById("b774d021-250a-4c3a-9c58-ab39edb36de5")
            assertEquals(retrievedEntity.productId.isNotEmpty(), true)
        }
    }

    @Test
    fun `test get all selected room`() {
        runBlocking {
            val entity = Cart(
                "1",
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
                true
            )
            dao.addToCart(entity)

            // Retrieve data and assert
            val retrievedEntity = dao.getSelected().first()
            assertEquals(retrievedEntity[0], entity)
        }
    }

    @Test
    fun `test get total room`() {
        runBlocking {
            val entity = Cart(
                "1",
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
                true
            )

            dao.addToCart(entity)

            // Retrieve data and assert
            val retrievedEntity = dao.getTotal()
            assertEquals(retrievedEntity, entity.productPrice)
        }
    }

    @Test
    fun `test delete by id room`() = runBlocking {
        val firstCart = Cart(
            "1",
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
            true
        )
        val secondCart = Cart(
            "2",
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
            true
        )

        dao.addToCart(firstCart)
        dao.addToCart(secondCart)

        dao.deleteById("1")
        val result = dao.getAllCart().first()
        assertEquals(result[0].productId != "1", true)
    }

    @Test
    fun `test delete all room`() = runBlocking {
        val firstCart = Cart(
            "1",
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
            true
        )
        val secondCart = Cart(
            "2",
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
            true
        )

        dao.addToCart(firstCart)
        dao.addToCart(secondCart)

        dao.deleteAllCart()
        val result = dao.getAllCart().first()
        assertEquals(result.isEmpty(), true)
    }

    @Test
    fun `test delete selected room`() = runBlocking {
        val firstCart = Cart(
            "1",
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
        val secondCart = Cart(
            "2",
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
            true
        )

        dao.addToCart(firstCart)
        dao.addToCart(secondCart)

        dao.deleteBySelected()
        val result = dao.getAllCart().first()
        assertEquals(result.any { it.productId != "2" }, true)
    }

    @Test
    fun `test updated quantity room`() = runBlocking {
        val firstCart = Cart(
            "1",
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
        val secondCart = Cart(
            "2",
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
            true
        )

        dao.addToCart(firstCart)
        dao.addToCart(secondCart)

        dao.updateQuantity("1", 3)
        val result = dao.getAllCart().first()
        assertEquals(result.any { it.quantity == 3 }, true)
    }

    @Test
    fun `test updated selected room`() = runBlocking {
        val firstCart = Cart(
            "1",
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
        val secondCart = Cart(
            "2",
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

        dao.addToCart(firstCart)
        dao.addToCart(secondCart)

        dao.updateChecked("1", true)
        val result = dao.getSelected().first()
        assertEquals(result.isNotEmpty(), true)
    }

    @Test
    fun `test updated checked all room`() = runBlocking {
        val firstCart = Cart(
            "1",
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
        val secondCart = Cart(
            "2",
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

        dao.addToCart(firstCart)
        dao.addToCart(secondCart)

        dao.updateCheckedAll(true)
        val result = dao.getSelected().first()
        assertEquals(result.any { it.selected == true }, true)
    }
}
