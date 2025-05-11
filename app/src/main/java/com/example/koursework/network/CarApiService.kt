package com.example.koursework.network

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import kotlin.io.encoding.Base64
import com.google.gson.annotations.SerializedName

data class CarResponse(
    val id: Long,
    val name: String,
    val price: String,
    @SerializedName("image")
    val imageBase64: String?, // <-- теперь правильно свяжется с JSON
    val description: String?,
    val consumption: String,
    val seats: Int,
    val co2: String
)

data class CarRequest(
    val name: String,
    val price: String,
    val image: String?, // base64-строка
    val description: String?,
    val consumption: String,
    val seats: Int,
    val co2: String
)

interface CarApiService {

    @GET("cars")
    suspend fun getCars(): List<CarResponse>

    @GET("cars/{id}")
    suspend fun getCarById(@Path("id") id: Long): Response<CarResponse>

    @POST("cars")
    suspend fun createCar(@Body car: CarRequest): Response<CarResponse>

    @PUT("cars/{id}")
    suspend fun updateCar(
        @Path("id") id: Long,
        @Body car: CarRequest
    ): Response<CarResponse>

    @DELETE("cars/{id}")
    suspend fun deleteCar(@Path("id") id: Long): Response<Unit>

    @POST("assignments")
    suspend fun assignCarToUser(
        @retrofit2.http.Query("carId") carId: Long,
        @retrofit2.http.Query("email") email: String
    ): Response<String>

}
