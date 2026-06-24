package com.example.classtask.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.classtask.R
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

// Firebase auth will be enabled in a future class once google-services.json is added
class SessionViewModel(application: Application) : AndroidViewModel(application) {

    private val _authenticated = MutableStateFlow<UiState<String>>(UiState.Loading)
    val authenticated: StateFlow<UiState<String>> = _authenticated.asStateFlow()

    fun createAccount(username: String, password: String) {
        if (username.isEmpty() || password.isEmpty()) {
            _authenticated.value = UiState.Error(application.getString(R.string.login_error_empty))
            return
        }
        // TODO: Firebase.auth.createUserWithEmailAndPassword(username, password)
        _authenticated.value = UiState.Error("Firebase not configured yet")
    }

    fun loginAccount(username: String, password: String) {
        if (username.isEmpty() || password.isEmpty()) {
            _authenticated.value = UiState.Error(application.getString(R.string.login_error_empty))
            return
        }
        // TODO: Firebase.auth.signInWithEmailAndPassword(username, password)
        _authenticated.value = UiState.Error("Firebase not configured yet")
    }
}
