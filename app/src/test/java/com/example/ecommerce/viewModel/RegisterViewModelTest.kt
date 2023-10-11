package com.example.ecommerce.viewModel

import android.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.core.api.request.AuthRequest
import com.example.core.api.response.BaseResponse
import com.example.core.api.response.RegisterResponse
import com.example.core.datastore.DataStoreRepository
import com.example.core.firebase.MessagingRepository
import com.example.core.util.Constant
import com.example.ecommerce.api.repository.AuthRepository
import com.example.ecommerce.auth.register.RegisterViewModel
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
import org.mockito.kotlin.whenever
import retrofit2.Response

@RunWith(AndroidJUnit4::class)
class RegisterViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var registerRepository: AuthRepository
    private lateinit var repository: DataStoreRepository
    private lateinit var messagingRepository: MessagingRepository
    private lateinit var analyticsRepository: AnalyticsRepository
    private lateinit var registerViewModel: RegisterViewModel

    @Before
    fun setUp() {
        registerRepository = Mockito.mock()
        repository = Mockito.mock()
        messagingRepository = Mockito.mock()
        analyticsRepository = Mockito.mock()
        registerViewModel = Mockito.mock()
    }

    @Test
    fun `test register view model`() = runTest {
        val data = RegisterResponse(
            200,
            "OK",
            RegisterResponse.Data("", "", 3600)
        )
        val expected = MutableLiveData<BaseResponse<RegisterResponse>>()
        expected.value = BaseResponse.Success(data)

        whenever(
            registerRepository.registerUser(
                Constant.API_KEY,
                AuthRequest("test@gmail.com", "12345678", "123")
            )
        ).thenReturn(Response.success(data))

        registerViewModel.registerUser(
            Constant.API_KEY,
            AuthRequest("", "", "")
        )

        backgroundScope.launch {
            val actual = registerViewModel.registerResult
            actual.observeForTesting {
                assertEquals(
                    Response.success(data).body(),
                    (actual.value as BaseResponse.Success).data
                )
            }
        }
    }
}
