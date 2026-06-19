package com.example.classtask.data.model

import com.squareup.moshi.Json

data class UnsplashCollection(
    val id: String,
    val title: String,
    @Json(name = "total_photos") val totalPhotos: Int,
    @Json(name = "cover_photo") val coverPhoto: UnsplashItem?,
    val user: User
)
