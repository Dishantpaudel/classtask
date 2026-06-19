package com.example.classtask.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.classtask.data.UnsplashApiProvider
import com.example.classtask.data.model.UnsplashCollection
import com.example.classtask.data.model.UnsplashItem
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn

@OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
class MainViewModel : ViewModel() {

    private val provider = UnsplashApiProvider()

    private val _query = MutableStateFlow("")
    val query: StateFlow<String> = _query.asStateFlow()

    private val _refreshSignal = MutableStateFlow(0)

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing.asStateFlow()

    val images: StateFlow<List<UnsplashItem>> = combine(_query.debounce(500), _refreshSignal) { q, _ -> q }
        .flatMapLatest { q ->
            _isRefreshing.value = true
            if (q.isBlank()) provider.fetchImages()
            else provider.searchImages(q)
        }
        .onEach { _isRefreshing.value = false }
        .catch { _isRefreshing.value = false; emit(emptyList()) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val collections: StateFlow<List<UnsplashCollection>> = provider.fetchCollections()
        .catch { emit(emptyList()) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun onQueryChange(newQuery: String) {
        _query.value = newQuery
    }

    fun refresh() {
        _refreshSignal.value++
    }
}
