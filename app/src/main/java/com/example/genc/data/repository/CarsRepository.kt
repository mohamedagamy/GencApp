package com.example.data.repository

import com.example.genctask.data.Car
import com.example.genctask.data.CarSearchResponse
import com.example.data.api.ApiService
import dagger.hilt.android.scopes.ActivityRetainedScoped

import javax.inject.Inject

@ActivityRetainedScoped
class CarsRepository @Inject constructor(
    private val apiService: ApiService,
) {
    suspend fun getCars(): CarSearchResponse= apiService.getCars()
}