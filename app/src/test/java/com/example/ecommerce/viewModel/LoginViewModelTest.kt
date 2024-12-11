package com.example.ecommerce.viewModel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.core.api.request.AuthRequest
import com.example.core.api.response.BaseResponse
import com.example.core.api.response.FirebaseResponse
import com.example.core.api.response.LoginResponse
import com.example.core.datastore.DataStoreRepository
import com.example.core.firebase.MessagingRepository
import com.example.core.util.Constant
import com.example.ecommerce.repository.AuthRepository
import com.example.ecommerce.auth.login.LoginViewModel
import com.example.ecommerce.firebase.AnalyticsRepository
import com.example.ecommerce.util.MainDispatcherRule
import com.example.ecommerce.util.observeForTesting
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import retrofit2.Response

@RunWith(AndroidJUnit4::class)
class LoginViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var loginRepository: AuthRepository
    private lateinit var repository: DataStoreRepository
    private lateinit var messagingRepository: MessagingRepository
    private lateinit var analyticsRepository: AnalyticsRepository
    private lateinit var loginViewModel: LoginViewModel

    @Before
    fun setUp() {
        loginRepository = mock()
        repository = Mockito.mock()
        messagingRepository = Mockito.mock()
        analyticsRepository = Mockito.mock()
        loginViewModel =
            LoginViewModel(loginRepository, repository, messagingRepository, analyticsRepository)
    }

    @Test
    fun `test login user view model`() = runTest {
        val data = LoginResponse(
            200,
            "OK",
            LoginResponse.LoginData("", "", "", "", 3600)
        )
        val expected = MutableLiveData<BaseResponse<LoginResponse>>()
        expected.value = BaseResponse.Success(data)

        whenever(
            loginRepository.loginUser(
                Constant.API_KEY,
                AuthRequest("test@gmail.com", "12345678", "123")
            )
        ).thenReturn(Response.success(data))

        loginViewModel.loginUser(
            Constant.API_KEY,
            AuthRequest("", "", "")
        )

        backgroundScope.launch {
            val actual = loginViewModel.loginResult
            actual.observeForTesting {
                assertEquals(
                    Response.success(data).body(),
                    (actual.value as BaseResponse.Success).data
                )
            }
        }
    }

    @Test
    fun `test token view model`() = runTest {
        val data = FirebaseResponse(
            200,
            "Update Berhasil"
        )
        val expected = MutableLiveData<BaseResponse<FirebaseResponse>>()
        expected.value = BaseResponse.Success(data)

        whenever(
            messagingRepository.firebaseToken(Constant.AUTH_TOKEN)
        ).thenReturn(Response.success(data))

        loginViewModel.token()

        backgroundScope.launch {
            val actual = loginViewModel.tokenResult
            actual.observeForTesting {
                assertEquals(
                    Response.success(data).body(),
                    (actual.value as BaseResponse.Success).data
                )
            }
        }
    }
}
