package com.example.koursework.network

data class FavoriteResponse(
    val id: Long,
    val user: UserDto,
    val car: CarDto
)

data class UserDto(
    val id: Long,
    val email: String,
    val role: String
)

data class CarDto(
    val id: Long,
    val name: String,
    val price: String,
    val consumption: String,
    val seats: Int,
    val co2: String,
    val imageBase64: String?
)
