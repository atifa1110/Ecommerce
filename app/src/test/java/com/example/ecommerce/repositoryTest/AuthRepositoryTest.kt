package com.example.ecommerce.repositoryTest

import com.example.core.api.method.ApiService
import com.example.core.api.model.RefreshToken
import com.example.core.api.request.AuthRequest
import com.example.core.api.response.LoginResponse
import com.example.core.api.response.ProfileResponse
import com.example.core.api.response.RefreshResponse
import com.example.core.api.response.RegisterResponse
import com.example.core.util.Constant
import com.example.ecommerce.api.repository.AuthRepository
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.runBlocking
import okhttp3.MultipartBody
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import retrofit2.Response

class AuthRepositoryTest {

    private lateinit var apiService: ApiService
    private lateinit var authRepository: AuthRepository

    @Before
    fun setup() {
        apiService = mock()
        authRepository = AuthRepository(apiService)
    }

    @Test
    fun `test login user repository`() {
        runBlocking {
            val expected = LoginResponse(
                200,
                "OK",
                LoginResponse.LoginData("", "", "", "", 3600)
            )
            whenever(
                apiService.loginUser(
                    Constant.API_KEY,
                    AuthRequest("a@gmail.com", "12345678", "123")
                )
            ).thenReturn(Response.success(expected))

            val result = authRepository.loginUser(
                Constant.API_KEY,
                AuthRequest("a@gmail.com", "12345678", "123")
            ).body()

            assertEquals(expected, result)
        }
    }

    @Test
    fun `test register user repository`() {
        runBlocking {
            val expected = RegisterResponse(
                200,
                "OK",
                RegisterResponse.Data("", "", 3600)
            )
            whenever(
                apiService.registerUser(
                    Constant.API_KEY,
                    AuthRequest("a@gmail.com", "12345678", "123")
                )
            ).thenReturn(Response.success(expected))

            val result = authRepository.registerUser(
                Constant.API_KEY,
                AuthRequest("a@gmail.com", "12345678", "123")
            ).body()

            assertEquals(expected, result)
        }
    }

    @Test
    fun `test profile register user repository`() {
        runBlocking {
            val expected = ProfileResponse(
                200,
                "OK",
                ProfileResponse.Data("", "")
            )
            // val file = File("")
            val image = MultipartBody.Part.createFormData(
                "userImage",
                ""
            )
            val name = MultipartBody.Part.createFormData(
                "userName",
                "Test"
            )
            whenever(
                apiService.profileUser(image, name)
            ).thenReturn(Response.success(expected))

            val result = authRepository.profileUser(image, name).body()

            assertEquals(expected, result)
        }
    }

    @Test
    fun `test refresh token user repository`() {
        runBlocking {
            val expected = RefreshResponse(
                200,
                "OK",
                RefreshResponse.Data("", "", 3600)
            )

            whenever(
                apiService.refreshToken(Constant.API_KEY, RefreshToken("123"))
            ).thenReturn(Response.success(expected))

            val result = authRepository.refresh(Constant.API_KEY, RefreshToken("123")).body()

            assertEquals(expected, result)
        }
    }
}
