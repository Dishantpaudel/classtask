package com.example.classtask.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.classtask.data.UnsplashApiProvider
import com.example.classtask.data.model.PhotoDetail
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class DetailsViewModel(private val photoId: String) : ViewModel() {

    private val provider = UnsplashApiProvider()

    private val _detail = MutableStateFlow<PhotoDetail?>(null)
    val detail: StateFlow<PhotoDetail?> = _detail.asStateFlow()

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    init {
        viewModelScope.launch {
            provider.getPhotoDetails(photoId)
                .catch { _isLoading.value = false }
                .collect {
                    _detail.value = it
                    _isLoading.value = false
                }
        }
    }

    companion object {
        fun factory(photoId: String): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T =
                    DetailsViewModel(photoId) as T
            }
    }
}
