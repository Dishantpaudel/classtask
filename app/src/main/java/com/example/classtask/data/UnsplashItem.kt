package com.example.classtask.data

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.classtask.data.converters.UrlConverter
import com.example.classtask.data.converters.UserConverter
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity
data class UnsplashItem(
    val blur_hash: String?,
    val color: String?,
    val created_at: String?,
    val description: String?,
    val height: Int?,
    @PrimaryKey
    val id: String,
    val updated_at: String?,
    @field:TypeConverters(UrlConverter::class)
    val urls: Urls?,
    @field:TypeConverters(UserConverter::class)
    val user: User?,
    val width: Int?
) : Parcelable
