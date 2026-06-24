package com.example.classtask.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.classtask.api.UnsplashProvider
import com.example.classtask.data.UnsplashItem
import com.example.classtask.repository.AppDatabase
import com.example.classtask.repository.UnsplashRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class UnsplashViewModel(application: Application) : AndroidViewModel(application) {

    private val _images = MutableStateFlow<UiState<List<UnsplashItem>>>(UiState.Loading)
    val images: StateFlow<UiState<List<UnsplashItem>>> = _images.asStateFlow()

    private val provider = UnsplashProvider()

    private val appDatabase = AppDatabase.getDatabase(application)
    private val dbRepository = UnsplashRepository(appDatabase.unsplashDao())

    init {
        fetchImage()
    }

    fun refreshingImages() {
        _images.value = UiState.Refreshing
        fetchImage()
    }

    fun fetchImage() {
        viewModelScope.launch {
            provider.fetchImages()
                .collect { images ->
                    _images.value = UiState.Success(images)
                    for (image in images) {
                        dbRepository.insertImage(image)
                    }
                }
        }
    }

    fun getImages() {
        viewModelScope.launch {
            dbRepository.allImages.collect { images ->
                _images.value = UiState.Success(images)
            }
        }
    }

    fun search(query: String) {
        viewModelScope.launch {
            provider.search(query)
                .collect { images ->
                    _images.value = UiState.Success(images)
                }
        }
    }
}
