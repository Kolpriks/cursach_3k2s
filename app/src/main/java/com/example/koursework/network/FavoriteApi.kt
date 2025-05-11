package com.example.koursework.network

import retrofit2.Response
import retrofit2.http.*

interface FavoriteApi {
    @GET("favorites")
    suspend fun getAllFavorites(): List<FavoriteResponse>

    @POST("favorites")
    suspend fun createFavorite(
        @Query("userId") userId: Long,
        @Query("carId") carId: Long
    ): Response<FavoriteResponse>

    @DELETE("favorites/{id}")
    suspend fun deleteFavorite(@Path("id") id: Long): Response<Void>
}
