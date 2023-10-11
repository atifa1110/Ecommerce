package com.example.core.api.response

import android.os.Parcelable
import com.example.core.api.model.Payment
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class PaymentResponse(
    @SerializedName("code")
    var code: Int,
    @SerializedName("message")
    var message: String,
    @SerializedName("data")
    var data: List<Payment>
) : Parcelable