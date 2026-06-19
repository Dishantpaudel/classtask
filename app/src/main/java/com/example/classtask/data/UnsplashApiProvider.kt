package com.example.classtask.data

import com.example.classtask.BuildConfig
import com.example.classtask.data.api.UnsplashApi
import com.example.classtask.data.model.PhotoDetail
import com.example.classtask.data.model.UnsplashCollection
import com.example.classtask.data.model.UnsplashItem
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

private const val BASE_URL = "https://api.unsplash.com/"

private val api: UnsplashApi by lazy {
    val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    val interceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }
    val authInterceptor = okhttp3.Interceptor { chain ->
        val request = chain.request().newBuilder()
            .addHeader("Authorization", "Client-ID ${BuildConfig.UNSPLASH_ACCESS_KEY}")
            .build()
        chain.proceed(request)
    }
    val client = OkHttpClient.Builder()
        .addInterceptor(authInterceptor)
        .addInterceptor(interceptor)
        .build()

    Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(client)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()
        .create(UnsplashApi::class.java)
}

class UnsplashApiProvider {

    fun fetchImages(): Flow<List<UnsplashItem>> = flow {
        emit(api.fetchPhotos())
    }.flowOn(Dispatchers.IO)

    fun searchImages(query: String): Flow<List<UnsplashItem>> = flow {
        emit(api.searchPhotos(query).results)
    }.flowOn(Dispatchers.IO)

    fun getPhotoDetails(id: String): Flow<PhotoDetail> = flow {
        emit(api.getPhoto(id))
    }.flowOn(Dispatchers.IO)

    fun fetchCollections(): Flow<List<UnsplashCollection>> = flow {
        emit(api.fetchCollections())
    }.flowOn(Dispatchers.IO)
}
