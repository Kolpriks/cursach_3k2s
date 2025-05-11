package com.example.koursework.network.AuthModels

data class RegisterRequest(val email: String, val password: String)
data class RegisterResponse(val id: Long, val email: String, val role: String)

data class LoginRequest(val email: String, val password: String)
data class LoginResponse(val id: Long, val email: String, val role: String)
