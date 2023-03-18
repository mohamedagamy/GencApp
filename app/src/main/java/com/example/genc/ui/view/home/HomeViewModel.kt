package com.example.genc.ui.view.home

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.genctask.data.Car
import com.example.genc.domain.getcars.GetCarsUseCase
import com.example.data.common.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class CarsViewModel @Inject constructor(
    private val carsUseCase: GetCarsUseCase,
) : ViewModel() {

    val state = mutableStateOf<CarsListUiState>(Loading)
    private var _query: String = ""

    init {
        getCars()
    }

    fun searchCars(query: String) = viewModelScope.launch {
        _query = query
        carsUseCase(_query).collect {
            state.value = CarsListUiStateReady(cars = it)
        }
    }

    fun filterCars(filter: Boolean) = viewModelScope.launch {
       carsUseCase(_query).collect {
            state.value = CarsListUiStateReady(cars = it)
        }
    }

    fun getCars() = viewModelScope.launch {
        withContext(Dispatchers.IO) {
            carsUseCase().collect(::handleResponse)
        }
    }

    private suspend fun handleResponse(it: Resource<List<Car>>) = withContext(Dispatchers.Main) {
        when (it.status) {
            Resource.Status.LOADING -> state.value = Loading
            Resource.Status.SUCCESS -> state.value = CarsListUiStateReady(cars = it.data)
            Resource.Status.ERROR -> state.value =
                CarsListUiStateError(error = it.error?.message)
        }
    }
}

sealed class CarsListUiState
data class CarsListUiStateReady(val cars: List<Car>?) : CarsListUiState()
object Loading : CarsListUiState()
class CarsListUiStateError(val error: String? = null) : CarsListUiState()