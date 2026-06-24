package com.example.classtask.api

import com.example.classtask.data.SearchResult
import com.example.classtask.data.UnsplashItem
import retrofit2.http.GET
import retrofit2.http.Query

interface UnsplashApi {

    @GET("photos")
    suspend fun fetchImages(): List<UnsplashItem>

    @GET("search/photos")
    suspend fun search(@Query("query") query: String): SearchResult
}
