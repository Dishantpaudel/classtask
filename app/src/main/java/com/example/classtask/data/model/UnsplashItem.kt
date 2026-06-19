package com.example.classtask.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

data class SearchResponse(
    val results: List<UnsplashItem>
)

@Parcelize
data class UnsplashItem(
    val id: String,
    val description: String?,
    val likes: Int,
    val urls: Urls,
    val user: User
) : Parcelable

@Parcelize
data class Urls(
    val raw: String,
    val full: String,
    val regular: String,
    val small: String,
    val thumb: String
) : Parcelable
