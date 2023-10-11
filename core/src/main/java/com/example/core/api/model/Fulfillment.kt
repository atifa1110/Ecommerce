package com.example.core.api.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Fulfillment(
    val invoiceId : String,
    val status : Boolean,
    val date : String,
    val time : String,
    val payment : String,
    val total : Int
) : Parcelable
