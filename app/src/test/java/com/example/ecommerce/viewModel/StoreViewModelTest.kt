package com.example.ecommerce.viewModel

import android.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.core.api.model.RefreshToken
import com.example.core.api.response.BaseResponse
import com.example.core.api.response.RefreshResponse
import com.example.core.api.response.SearchResponse
import com.example.core.util.Constant
import com.example.ecommerce.api.repository.AuthRepository
import com.example.ecommerce.api.repository.ProductRepository
import com.example.ecommerce.firebase.AnalyticsRepository
import com.example.ecommerce.main.store.StoreViewModel
import com.example.ecommerce.util.MainDispatcherRule
import com.example.ecommerce.util.observeForTesting
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.mockito.kotlin.whenever
import retrofit2.Response
import kotlin.test.assertEquals

@RunWith(AndroidJUnit4::class)
class StoreViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var productRepository: ProductRepository
    private lateinit var authRepository: AuthRepository
    private lateinit var dataStoreRepository: com.example.core.datastore.DataStoreRepository
    private lateinit var analyticsRepository: AnalyticsRepository
    private lateinit var storeViewModel: StoreViewModel

    @Before
    fun setUp() {
        productRepository = Mockito.mock()
        authRepository = Mockito.mock()
        dataStoreRepository = Mockito.mock()
        analyticsRepository = Mockito.mock()
        storeViewModel = Mockito.mock()
        storeViewModel = StoreViewModel(
            productRepository,
            authRepository,
            dataStoreRepository,
            analyticsRepository
        )
    }

    @Test
    fun `test search product list view model`() = runTest {
        val data = SearchResponse(
            200,
            "OK",
            arrayListOf("lenovo1", "lenovo2", "lenovo3")
        )

        val expected = MutableLiveData<BaseResponse<SearchResponse>>()
        expected.value = BaseResponse.Success(data)

        whenever(
            productRepository.searchProductList("lenovo")
        ).thenReturn(Response.success(data))

        storeViewModel.searchProductList("lenovo")

        backgroundScope.launch {
            val actual = storeViewModel.searchResult
            actual.observeForTesting {
                assertEquals(
                    Response.success(data).body(),
                    (actual.value as BaseResponse.Success).data
                )
            }
        }
    }

    @Test
    fun `test refresh token view model`() = runTest {
        val data = RefreshResponse(
            200,
            "OK",
            RefreshResponse.Data("", "", 3600)
        )

        val expected = MutableLiveData<BaseResponse<RefreshResponse>>()
        expected.value = BaseResponse.Success(data)

        whenever(
            authRepository.refresh(Constant.API_KEY, RefreshToken("123"))
        ).thenReturn(Response.success(data))

        storeViewModel.refreshToken()

        backgroundScope.launch {
            val actual = storeViewModel.tokenResult
            actual.observeForTesting {
                assertEquals(
                    Response.success(data).body(),
                    (actual.value as BaseResponse.Success).data
                )
            }
        }
    }
}
