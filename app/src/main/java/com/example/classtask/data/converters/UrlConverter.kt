package com.example.classtask.data.converters

import androidx.room.TypeConverter
import com.example.classtask.data.Urls
import com.squareup.moshi.Moshi
import com.squareup.moshi.adapter
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

class UrlConverter {

    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    @OptIn(ExperimentalStdlibApi::class)
    private val jsonAdapter = moshi.adapter<Urls>()

    @TypeConverter
    fun fromUrl(url: Urls): String {
        return jsonAdapter.toJson(url)
    }

    @TypeConverter
    fun toUrl(value: String): Urls? {
        return jsonAdapter.fromJson(value)
    }
}
