package com.example.classtask.data.api

import com.example.classtask.data.model.PhotoDetail
import com.example.classtask.data.model.SearchResponse
import com.example.classtask.data.model.UnsplashCollection
import com.example.classtask.data.model.UnsplashItem
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface UnsplashApi {

    @GET("photos")
    suspend fun fetchPhotos(
        @Query("per_page") perPage: Int = 10
    ): List<UnsplashItem>

    @GET("search/photos")
    suspend fun searchPhotos(
        @Query("query") query: String,
        @Query("per_page") perPage: Int = 10
    ): SearchResponse

    @GET("photos/{id}")
    suspend fun getPhoto(
        @Path("id") id: String
    ): PhotoDetail

    @GET("collections")
    suspend fun fetchCollections(
        @Query("per_page") perPage: Int = 10
    ): List<UnsplashCollection>
}
