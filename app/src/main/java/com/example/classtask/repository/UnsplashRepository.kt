package com.example.classtask.repository

import com.example.classtask.data.UnsplashItem

class UnsplashRepository(val dao: UnsplashDao) {

    val allImages = dao.getImages()

    fun insertImage(image: UnsplashItem) {
        AppDatabase.databaseExecuters.execute {
            dao.insertImage(image)
        }
    }
}
