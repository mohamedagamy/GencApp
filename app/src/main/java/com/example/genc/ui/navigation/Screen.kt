package com.example.genc.ui.navigation

sealed class Screen(val route: String) {
    object Cars : Screen("cars")
    object CarDetails : Screen("cars/{item}") {
        fun createRoute(car: String) = "cars/$car"
    }
}