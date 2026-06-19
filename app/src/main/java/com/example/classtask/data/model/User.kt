package com.example.classtask.data.model

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    val id: String,
    val username: String,
    val name: String,
    @Json(name = "profile_image") val profileImage: ProfileImage?
) : Parcelable

@Parcelize
data class ProfileImage(
    val medium: String
) : Parcelable
