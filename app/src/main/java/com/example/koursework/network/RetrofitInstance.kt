package com.example.koursework.network

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val client = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .build()

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("http://84.201.181.19:8080/") // адрес сервера
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val authApi: AuthApiService by lazy {
        retrofit.create(AuthApiService::class.java)
    }

    val api: CarApiService by lazy { retrofit.create(CarApiService::class.java) }
    val assignmentApi: AssignmentApiService by lazy { retrofit.create(AssignmentApiService::class.java) }
    val favoriteApi: FavoriteApi = retrofit.create(FavoriteApi::class.java)
}
