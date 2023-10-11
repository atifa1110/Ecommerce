package com.example.ecommerce.viewModel

import android.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.core.api.model.Transaction
import com.example.core.api.response.BaseResponse
import com.example.core.api.response.TransactionResponse
import com.example.core.room.cart.CartItem
import com.example.ecommerce.api.repository.PaymentRepository
import com.example.ecommerce.firebase.AnalyticsRepository
import com.example.ecommerce.main.transaction.TransactionViewModel
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
class TransactionViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var paymentRepository: PaymentRepository
    private lateinit var analyticsRepository: AnalyticsRepository
    private lateinit var transactionViewModel: TransactionViewModel

    @Before
    fun setUp() {
        paymentRepository = Mockito.mock()
        analyticsRepository = Mockito.mock()
        transactionViewModel = Mockito.mock()
        transactionViewModel = TransactionViewModel(paymentRepository, analyticsRepository)
    }

    @Test
    fun `test get transaction view model`() = runTest {
        val data = TransactionResponse(
            200,
            "OK",
            listOf(
                Transaction(
                    "c0aecf56-911c-4d4e-a5a2-f2ecd20f86be",
                    true,
                    "26 Sep 2023",
                    "15:03",
                    "Bank BCA",
                    13849000,
                    listOf(
                        CartItem(
                            "b774d021-250a-4c3a-9c58-ab39edb36de5",
                            "RAM 16GB",
                            1
                        )
                    ),
                    "",
                    "",
                    "image",
                    "name"
                )
            )
        )
        val expected = MutableLiveData<BaseResponse<TransactionResponse>>()
        expected.value = BaseResponse.Success(data)

        whenever(
            paymentRepository.transaction()
        ).thenReturn(Response.success(data))

        transactionViewModel.getAllTransaction()

        backgroundScope.launch {
            val actual = transactionViewModel.transactionResult
            actual.observeForTesting {
                assertEquals(
                    Response.success(data).body(),
                    (actual.value as BaseResponse.Success).data
                )
            }
        }
    }
}
