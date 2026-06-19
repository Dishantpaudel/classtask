package com.example.classtask.data.model

import com.squareup.moshi.Json

data class LocationInfo(
    val city: String?,
    val country: String?,
    val name: String?
)

data class Exif(
    val make: String?,
    val model: String?,
    val aperture: String?,
    @Json(name = "exposure_time") val exposureTime: String?,
    @Json(name = "focal_length") val focalLength: String?,
    val iso: Int?
)

data class PhotoTag(
    val title: String
)

data class PhotoDetail(
    val id: String,
    val description: String?,
    @Json(name = "alt_description") val altDescription: String?,
    val likes: Int?,
    val downloads: Int?,
    val views: Int?,
    val width: Int?,
    val height: Int?,
    val location: LocationInfo?,
    val exif: Exif?,
    val urls: Urls,
    val user: User,
    val tags: List<PhotoTag>?
)
