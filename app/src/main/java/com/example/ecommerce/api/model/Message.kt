package com.example.ecommerce.api.model

data class Message(
    val token: String,
    val notification: Notification,
    val data: NotificationData
)