package com.example.koursework.network

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

// Ответ от /assignments/count
data class AssignmentCountResponse(
    val managerEmail: String,
    val assignmentsCount: Long
)

interface AssignmentApiService {
    @POST("assignments")
    suspend fun assignCarToUser(
        @Query("carId") carId: Long,
        @Query("buyerEmail") email: String,       // ← параметр переименован
        @Query("managerEmail") managerEmail: String // ← добавили
    ): Response<Unit>

    @GET("assignments/count")
    suspend fun getCountByManager(
        @Query("managerEmail") managerEmail: String
    ): Response<AssignmentCountResponse>
}
