package com.example.genc.ui.view.details

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.genctask.data.Car
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(
    stateHandle: SavedStateHandle
) : ViewModel() {

    val state = mutableStateOf<DetailsUiState>(Loading)

    init {
        val item = Gson().fromJson(stateHandle.get<String>("item"),Car::class.java)
        item?.let {
            getCarDetails(it)
        }
    }

    private fun getCarDetails(car: Car) {
        viewModelScope.launch {
            state.value = DetailsUiStateReady(car = car)
        }
    }
}

sealed class DetailsUiState
data class DetailsUiStateReady(val car: Car) : DetailsUiState()
object Loading : DetailsUiState()
class DetailsUiStateError(val error: Car? = null) : DetailsUiState()