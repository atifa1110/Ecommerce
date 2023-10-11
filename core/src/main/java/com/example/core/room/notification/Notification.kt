package com.example.core.room.notification

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "notification")
@Parcelize
data class Notification(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id : Int? = null,
    @ColumnInfo(name = "title")
    val title: String,
    @ColumnInfo(name = "body")
    val body: String,
    @ColumnInfo(name = "image")
    val image: String,
    @ColumnInfo(name = "type")
    val type: String,
    @ColumnInfo(name = "date")
    val date: String,
    @ColumnInfo(name = "time")
    val time: String,
    @ColumnInfo(name = "isRead")
    val isRead : Boolean
) : Parcelable
