package com.example.ecommerce.serviceTest

import com.example.core.api.method.ApiService
import com.example.core.api.model.Product
import com.example.core.api.model.ProductDetail
import com.example.core.api.model.ProductVariant
import com.example.core.api.model.RefreshToken
import com.example.core.api.request.AuthRequest
import com.example.core.api.response.DetailResponse
import com.example.core.api.response.LoginResponse
import com.example.core.api.response.ProductResponse
import com.example.core.api.response.ProfileResponse
import com.example.core.api.response.RefreshResponse
import com.example.core.api.response.RegisterResponse
import com.example.core.api.response.ReviewResponse
import com.example.core.api.response.SearchResponse
import com.example.core.util.Constant
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.runBlocking
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okio.buffer
import okio.source
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.nio.charset.StandardCharsets

@RunWith(JUnit4::class)
class ApiServiceTest {

    private lateinit var mockWebServer: MockWebServer
    private lateinit var apiService: ApiService

    @Before
    fun setup() {
        mockWebServer = MockWebServer()
        val client = OkHttpClient.Builder()
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        apiService = retrofit.create(ApiService::class.java)
    }

    @After
    fun teardown() {
        mockWebServer.shutdown()
    }

    @Test
    fun `test login service`() {
        mockWebServer.enqueueResponse("Login.json", 200)
        runBlocking {
            val actual = apiService.loginUser(
                Constant.API_KEY,
                AuthRequest(
                    "test@gmail.com",
                    "12345678",
                    ""
                )
            ).body()
            val expected = LoginResponse(
                200,
                "OK",
                LoginResponse.LoginData(
                    "",
                    "",
                    "123",
                    "123",
                    3600
                )
            )
            assertEquals(expected, actual)
        }
    }

    @Test
    fun `test register service`() {
        mockWebServer.enqueueResponse("Register.json", 200)
        runBlocking {
            val actual = apiService.registerUser(
                Constant.API_KEY,
                AuthRequest(
                    "test@gmail.com",
                    "12345678",
                    ""
                )
            ).body()
            val expected = RegisterResponse(
                200,
                "OK",
                RegisterResponse.Data(
                    "123",
                    "123",
                    3600
                )
            )
            assertEquals(expected, actual)
        }
    }

    @Test
    fun `test refresh token service`() {
        mockWebServer.enqueueResponse("Refresh.json", 200)
        runBlocking {
            val actual = apiService.refreshToken(Constant.API_KEY, RefreshToken("123")).body()
            val expected = RefreshResponse(
                200,
                "OK",
                RefreshResponse.Data(
                    "123",
                    "123",
                    3600
                )
            )
            assertEquals(expected, actual)
        }
    }

    @Test
    fun `test profile service`() {
        mockWebServer.enqueueResponse("Profile.json", 200)
        runBlocking {
            val file = File("")
            val image = MultipartBody.Part.createFormData(
                "userImage",
                ""
            )
            val name = MultipartBody.Part.createFormData(
                "userName",
                "Test"
            )
            val actual = apiService.profileUser(image, name).body()
            val expected = ProfileResponse(
                200,
                "OK",
                ProfileResponse.Data(
                    "Test",
                    "http://192.168.190.125:8080/static/images/"
                )
            )

            assertEquals(expected, actual)
        }
    }

    @Test
    fun `test product filter service`() {
        mockWebServer.enqueueResponse("Products.json", 200)
        runBlocking {
            val actual = apiService.getProductFilter(
                "",
                "",
                0,
                0,
                "",
                0,
                0
            ).body()
            val expected = ProductResponse(
                200,
                "OK",
                ProductResponse.Data(
                    10,
                    4,
                    1,
                    1,
                    arrayListOf(
                        Product(
                            "1",
                            "Product 1",
                            13849000,
                            "images",
                            "Dell",
                            "DellStore",
                            13,
                            5.0F
                        )
                    )
                )
            )

            assertEquals(expected, actual)
        }
    }

    @Test
    fun `test search product service`() {
        mockWebServer.enqueueResponse("Search.json", 200)
        runBlocking {
            val actual = apiService.searchProductList("lenovo").body()
            val expected = SearchResponse(
                200,
                "OK",
                arrayListOf(
                    "Lenovo Ideapad Gaming",
                    "Lenovo Ideapad Slim",
                    "Lenovo Ideapad Slim",
                    "Lenovo Legion Pro",
                    "Lenovo Legion Pro",
                    "Lenovo Yoga 6",
                    "Lenovo Yoga 7i",
                    "Lenovo Yoga 9i"
                )
            )

            assertEquals(expected, actual)
        }
    }

    @Test
    fun `test detail product service`() {
        mockWebServer.enqueueResponse("Detail.json", 200)
        runBlocking {
            val actual = apiService.getProductDetail("b774d021-250a-4c3a-9c58-ab39edb36de5").body()
            val expected = DetailResponse(
                200,
                "OK",
                ProductDetail(
                    "b774d021-250a-4c3a-9c58-ab39edb36de5",
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
            assertEquals(expected, actual)
        }
    }

    @Test
    fun `test review product service`() {
        mockWebServer.enqueueResponse("Review.json", 200)
        runBlocking {
            val actual = apiService.getProductReview("b774d021-250a-4c3a-9c58-ab39edb36de5").body()
            val expected = ReviewResponse(
                200,
                "OK",
                listOf(
                    ReviewResponse.Review(
                        "John",
                        "image",
                        4,
                        "review"
                    ),
                    ReviewResponse.Review(
                        "Doe",
                        "image",
                        5,
                        "review"
                    )
                )
            )
            assertEquals(expected, actual)
        }
    }
}

fun MockWebServer.enqueueResponse(fileName: String, code: Int) {
    val inputStream = javaClass.classLoader?.getResourceAsStream(fileName)
    val source = inputStream?.let { inputStream.source().buffer() }
    source?.let {
        enqueue(
            MockResponse()
                .setResponseCode(code)
                .setBody(source.readString(StandardCharsets.UTF_8))
        )
    }
}
