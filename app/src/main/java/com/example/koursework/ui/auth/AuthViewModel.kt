package com.example.koursework.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.koursework.network.AuthModels.LoginRequest
import com.example.koursework.network.AuthModels.RegisterRequest
import com.example.koursework.network.RetrofitInstance
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class AuthResult {
    data class Success(val userId: Long, val role: String) : AuthResult()
    data class Error(val message: String) : AuthResult()
}

class AuthViewModel : ViewModel() {

    private val _registerResult = MutableStateFlow<AuthResult?>(null)
    val registerResult: StateFlow<AuthResult?> = _registerResult

    private val _loginResult = MutableStateFlow<AuthResult?>(null)
    val loginResult: StateFlow<AuthResult?> = _loginResult

    fun register(email: String, password: String) {
        viewModelScope.launch {
            try {
                val resp = RetrofitInstance.authApi.register(RegisterRequest(email, password))
                if (resp.isSuccessful && resp.body() != null) {
                    val body = resp.body()!!
                    _registerResult.value = AuthResult.Success(body.id, body.role)
                } else {
                    _registerResult.value = AuthResult.Error("Ошибка регистрации")
                }
            } catch (e: Exception) {
                _registerResult.value = AuthResult.Error(e.message ?: "Сетевая ошибка")
            }
        }
    }

    fun login(email: String, password: String) {
        viewModelScope.launch {
            try {
                val resp = RetrofitInstance.authApi.login(LoginRequest(email, password))
                if (resp.isSuccessful && resp.body() != null) {
                    val body = resp.body()!!
                    _loginResult.value = AuthResult.Success(body.id, body.role)
                } else {
                    _loginResult.value = AuthResult.Error("Неверная почта или пароль")
                }
            } catch (e: Exception) {
                _loginResult.value = AuthResult.Error(e.message ?: "Сетевая ошибка")
            }
        }
    }

    fun clearResults() {
        _loginResult.value = null
        _registerResult.value = null
    }
}
