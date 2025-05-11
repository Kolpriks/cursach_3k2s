package com.example.koursework.network

import com.example.koursework.network.AuthModels.LoginRequest
import com.example.koursework.network.AuthModels.LoginResponse
import com.example.koursework.network.AuthModels.RegisterRequest
import com.example.koursework.network.AuthModels.RegisterResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApiService {

    @POST("auth/register")
    suspend fun register(@Body body: RegisterRequest): Response<RegisterResponse>

    @POST("auth/login")
    suspend fun login(@Body body: LoginRequest): Response<LoginResponse>
}
