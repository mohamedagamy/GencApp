package com.example.genc.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.genc.ui.view.details.CarDetailsScreen
import com.example.genc.ui.view.home.CarSearchScreen

@Preview
@Composable
fun NavGraph() {
    val navController = rememberNavController()
    val actions = remember(navController) { MainActions(navController) }

    NavHost(navController, startDestination = Screen.Cars.route) {
        composable(Screen.Cars.route) { CarSearchScreen { actions.goToCarDetailsScreen.invoke(it) } }
        composable(Screen.CarDetails.route) { CarDetailsScreen() }
    }
}

class MainActions(private val navController: NavHostController) {

    val goToCarsScreen: () -> Unit = {
        navController.navigate(Screen.Cars.route)
    }

    val goToCarDetailsScreen: (String) -> Unit = {
        navController.navigate(Screen.CarDetails.createRoute(it))
    }
}