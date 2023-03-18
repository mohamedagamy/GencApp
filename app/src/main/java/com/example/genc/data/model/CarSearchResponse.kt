package com.example.genctask.data

data class CarSearchResponse(
    val cars: List<Car>? = emptyList(),
    val status: Status = Status()
)

data class Car(
    val brand: String = "",
    val color: String = "",
    val currency: String = "",
    val model: Int? = 0,
    val plate_number: String = "",
    val unit_price: Double = 0.0
)

data class Status(
    val code: Int = 0,
    val message: String = ""
)