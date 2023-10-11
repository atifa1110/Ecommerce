package com.example.ecommerce.auth.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.api.response.BaseResponse
import com.example.core.api.response.ProfileResponse
import com.example.core.datastore.DataStoreRepository
import com.example.ecommerce.api.repository.AuthRepository
import com.example.ecommerce.firebase.AnalyticsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import org.json.JSONObject
import java.io.File
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val profileRepository: AuthRepository,
    private val repository: DataStoreRepository,
    private val analyticsRepository: AnalyticsRepository
) : ViewModel() {

    private val _profileResult: MutableLiveData<BaseResponse<ProfileResponse>> = MutableLiveData()
    val profileResult: LiveData<BaseResponse<ProfileResponse>> get() = _profileResult

    fun getProfileUser(userImage: File, userName: String) {
        _profileResult.value = BaseResponse.Loading()
        viewModelScope.launch {
            try {
                val imagePart = MultipartBody.Part.createFormData(
                    "userImage",
                    userImage.name,
                    userImage.asRequestBody("image/*".toMediaType())
                )

                val usernamePart = MultipartBody.Part.createFormData(
                    "userName",
                    userName
                )

                val response = profileRepository.profileUser(imagePart, usernamePart)
                if (response.code() == 200 && response.isSuccessful) {
                    _profileResult.value = BaseResponse.Success(response.body())
                } else {
                    val jsonObj = JSONObject(response.errorBody()!!.string())
                    val message = jsonObj.getString("message")
                    _profileResult.value = BaseResponse.Error(message)
                }
            } catch (ex: Exception) {
                _profileResult.value = BaseResponse.Error(ex.message)
            }
        }
    }

    fun saveProfileName(name: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.saveProfileName(name)
        }
    }

    fun buttonAnalytics(buttonName: String) {
        viewModelScope.launch(Dispatchers.IO) {
            analyticsRepository.buttonClick(buttonName)
        }
    }
}
