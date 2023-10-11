package com.example.ecommerce.viewModel

import android.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.core.api.response.BaseResponse
import com.example.core.api.response.ProfileResponse
import com.example.core.datastore.DataStoreRepository
import com.example.ecommerce.api.repository.AuthRepository
import com.example.ecommerce.auth.profile.ProfileViewModel
import com.example.ecommerce.firebase.AnalyticsRepository
import com.example.ecommerce.util.MainDispatcherRule
import com.example.ecommerce.util.observeForTesting
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import okhttp3.MultipartBody
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.mockito.kotlin.whenever
import retrofit2.Response
import java.io.File
import kotlin.test.assertEquals

@RunWith(AndroidJUnit4::class)
class ProfileViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var profileRepository: AuthRepository
    private lateinit var repository: DataStoreRepository
    private lateinit var analyticsRepository: AnalyticsRepository
    private lateinit var profileViewModel: ProfileViewModel

    @Before
    fun setUp() {
        profileRepository = Mockito.mock()
        repository = Mockito.mock()
        analyticsRepository = Mockito.mock()
        profileViewModel = Mockito.mock()
        profileViewModel = ProfileViewModel(profileRepository, repository, analyticsRepository)
    }

    @Test
    fun `test profile user view model`() = runTest {
        val data = ProfileResponse(
            200,
            "OK",
            ProfileResponse.Data("", "")
        )

        val expected = MutableLiveData<BaseResponse<ProfileResponse>>()
        expected.value = BaseResponse.Success(data)

        val file = File("")
        val image = MultipartBody.Part.createFormData(
            "userImage",
            ""
        )
        val name = MultipartBody.Part.createFormData(
            "userName",
            "Test"
        )
        whenever(
            profileRepository.profileUser(image, name)
        ).thenReturn(Response.success(data))

        profileViewModel.getProfileUser(file, "name")

        backgroundScope.launch {
            val actual = profileViewModel.profileResult
            actual.observeForTesting {
                assertEquals(
                    Response.success(data).body(),
                    (actual.value as BaseResponse.Success).data
                )
            }
        }
    }
}
