package com.example.ecommerce.auth.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecommerce.api.response.BaseResponse
import com.example.ecommerce.api.response.ProfileResponse
import com.example.ecommerce.api.repository.AuthRepository
import com.example.ecommerce.datastore.DataStoreRepository
import com.example.ecommerce.util.Constant
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel@Inject constructor(
    private val profileRepository: AuthRepository,
    private val repository: DataStoreRepository
): ViewModel() {

    private val _profileResult : MutableLiveData<BaseResponse<ProfileResponse>> = MutableLiveData()
    val profileResult: LiveData<BaseResponse<ProfileResponse>> get() = _profileResult


    fun getProfileUser(userName: String, userImage:String){
        _profileResult.value = BaseResponse.Loading()
        viewModelScope.launch {
            try {
                val response = profileRepository.profileUser(userName,userImage)
                if (response.code() == 200 && response.isSuccessful) {
                    _profileResult.value = BaseResponse.Success(response.body())
                } else{
                    val jsonObj= JSONObject(response.errorBody()!!.string())
                    val message = jsonObj.getString("message")
                    _profileResult.value = BaseResponse.Error(message)
                }
            } catch (ex: Exception) {
                _profileResult.value = BaseResponse.Error(ex.message)
            }
        }
    }

    fun getToken(): String {
       return "Bearer ${repository.getAccessToken()}"
    }

    suspend fun saveProfileName(name: String){
        viewModelScope.launch(Dispatchers.IO) {
            repository.saveProfileName(name)
        }
    }
}