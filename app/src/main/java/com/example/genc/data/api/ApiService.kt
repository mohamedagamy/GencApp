package com.example.data.api

import com.example.genctask.data.Car
import com.example.genctask.data.CarSearchResponse
import retrofit2.http.*

interface ApiService {

    @GET("cars/list/")
    suspend fun getCars(): CarSearchResponse
}
