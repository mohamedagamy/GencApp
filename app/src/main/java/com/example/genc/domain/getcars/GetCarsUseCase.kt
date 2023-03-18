package com.example.genc.domain.getcars

import androidx.compose.ui.text.toLowerCase
import com.example.genctask.data.Car
import com.example.data.common.Resource
import com.example.data.repository.CarsRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.*
import javax.inject.Inject

class GetCarsUseCase @Inject constructor(
    private val repository: CarsRepository,
    private val defaultDispatcher: CoroutineDispatcher
) {

    private var carList = listOf<Car>()

    suspend operator fun invoke(): Flow<Resource<List<Car>>> =
        flow<Resource<List<Car>>> {
            try {
                emit(Resource.loading())
                carList = repository.getCars().cars!!
                emit(Resource.success(carList))
            } catch (e: Throwable) {
                emit(Resource.error(e))
            }
        }.flowOn(defaultDispatcher)

    suspend operator fun invoke(
        query: String,
    ): Flow<List<Car>> = flow {
        filter(flowOf(carList), query).collect { emit(it) }
    }

    private fun filter(
        response: Flow<List<Car>>,
        query: String,
    ): Flow<List<Car>> {
        val filter = response.map { it.filter { car -> car.unit_price.toString().lowercase().contains(query) || car.color.lowercase().contains(query) } }
        return filter.map{car -> car}.flowOn(defaultDispatcher)
    }
}

