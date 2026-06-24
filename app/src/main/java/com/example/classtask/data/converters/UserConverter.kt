package com.example.classtask.data.converters

import androidx.room.TypeConverter
import com.example.classtask.data.User
import com.squareup.moshi.Moshi
import com.squareup.moshi.adapter
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

class UserConverter {

    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    @OptIn(ExperimentalStdlibApi::class)
    private val jsonAdapter = moshi.adapter<User>()

    @TypeConverter
    fun fromUser(user: User): String {
        return jsonAdapter.toJson(user)
    }

    @TypeConverter
    fun toUser(value: String): User? {
        return jsonAdapter.fromJson(value)
    }
}
