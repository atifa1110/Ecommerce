package com.example.ecommerce.repositoryTest

import com.example.core.api.method.ApiService
import com.example.core.api.model.ProductDetail
import com.example.core.api.model.ProductVariant
import com.example.core.api.response.DetailResponse
import com.example.core.api.response.ReviewResponse
import com.example.core.api.response.SearchResponse
import com.example.ecommerce.repository.ProductRepository
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import retrofit2.Response

class ProductRepositoryTest {

    private lateinit var apiService: ApiService
    private lateinit var productRepository: ProductRepository

    @Before
    fun setup() {
        apiService = mock()
        productRepository = ProductRepository(apiService)
    }

//    @Test
//    fun `test get product filter repository`() {
//        runBlocking {
//            val expected = ProductResponse(
//                200,
//                "OK",
//                ProductResponse.Data(
//                    10, 2,
//                    0, 8,
//                    arrayListOf(
//                        Product(
//                            "1",
//                            "",
//                            100000,
//                            "image",
//                            "brand",
//                            "store",
//                            10,
//                            0F
//                        )
//                    )
//                )
//            )
//
//            whenever(
//                apiService.getProductFilter("lenovo", "", 0, 1000000, "", 10, 2)
//            ).thenReturn(Response.success(expected))
//
//            val result = productRepository.getProductFilter("lenovo", "", 0, 1000000, "").collect()
//
//            assertEquals(expected, result)
//        }
//    }

    @Test
    fun `test search product list repository`() {
        runBlocking {
            val expected = SearchResponse(
                200,
                "OK",
                arrayListOf("lenovo1", "lenovo2", "lenovo3")
            )

            whenever(
                apiService.searchProductList("lenovo")
            ).thenReturn(Response.success(expected))

            val result = productRepository.searchProductList("lenovo").body()

            assertEquals(expected, result)
        }
    }

    @Test
    fun `test get product detail repository`() {
        runBlocking {
            val expected = DetailResponse(
                200,
                "OK",
                ProductDetail(
                    "1",
                    "Dell",
                    13849000,
                    listOf("image1", "image2", "image3"),
                    "Dell",
                    "DELL",
                    "DellStore",
                    13,
                    44,
                    10,
                    5,
                    100,
                    5.0F,
                    listOf(
                        ProductVariant("RAM 16GB", 0),
                        ProductVariant("RAM 32GB", 1000000)
                    )
                )
            )

            whenever(
                apiService.getProductDetail("1")
            ).thenReturn(Response.success(expected))

            val result = productRepository.getProductDetail("1").body()

            assertEquals(expected, result)
        }
    }

    @Test
    fun `test get product review repository`() {
        runBlocking {
            val expected = ReviewResponse(
                200,
                "OK",
                listOf(
                    ReviewResponse.Review("a", "image", 5, "ok")
                )
            )

            whenever(
                apiService.getProductReview("1")
            ).thenReturn(Response.success(expected))

            val result = productRepository.getProductReview("1").body()

            assertEquals(expected, result)
        }
    }
}
