package com.example.ecommerce.viewModel

import android.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.ecommerce.api.repository.PaymentRepository
import com.example.ecommerce.firebase.AnalyticsRepository
import com.example.ecommerce.main.checkout.CheckoutViewModel
import com.example.ecommerce.room.cart.CartRepository
import com.example.ecommerce.util.MainDispatcherRule
import org.junit.Before
import org.junit.Rule
import org.mockito.Mockito

class CheckOutViewModelTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var cartRepository: CartRepository
    private lateinit var paymentRepository: PaymentRepository
    private lateinit var analyticsRepository: AnalyticsRepository
    private lateinit var checkoutViewModel: CheckoutViewModel

    @Before
    fun setUp() {
        cartRepository = Mockito.mock()
        paymentRepository = Mockito.mock()
        analyticsRepository = Mockito.mock()
        checkoutViewModel = Mockito.mock()
        checkoutViewModel =
            CheckoutViewModel(cartRepository, paymentRepository, analyticsRepository)
    }

//    @Test
//    fun `test fulfillment view model`() = runTest {
//        val data = FulfillmentResponse(
//            200,
//            "OK",
//            Fulfillment(
//                "c0aecf56-911c-4d4e-a5a2-f2ecd20f86be",
//                true,
//                "26 Sep 2023",
//                "15:03",
//                "Bank BCA",
//                13849000
//            )
//        )
//
//        val expected = MutableLiveData<BaseResponse<FulfillmentResponse>>()
//        expected.value = BaseResponse.Success(data)
//
//        whenever(
//            paymentRepository.fulfillment(
//                FulfillmentRequest(
//                    "",
//                    listOf(
//                        CartItem(
//                            "c0aecf56-911c-4d4e-a5a2-f2ecd20f86be",
//                            "RAM 16GB",
//                            1
//                        )
//                    )
//                )
//            )
//        ).thenReturn(Response.success(data))
//
//        checkoutViewModel.fulfillment(
//            FulfillmentRequest(
//                "",
//                listOf(
//                    CartItem(
//                        "c0aecf56-911c-4d4e-a5a2-f2ecd20f86be",
//                        "RAM 16GB",
//                        1
//                    )
//                )
//            )
//        )
//
//        backgroundScope.launch {
//            val actual = checkoutViewModel.checkOutResult
//            actual.observeForTesting {
//                assertEquals(
//                    Response.success(data).body(),
//                    (actual.value as BaseResponse.Success).data
//                )
//            }
//        }
//    }
}
