package cst.unibucfmi2026.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import cst.unibucfmi2026.ui.pages.HomePage
import cst.unibucfmi2026.ui.pages.LoginPage
import cst.unibucfmi2026.ui.pages.RegisterPage


@Composable
fun AuthNavigation() {
    val navController = rememberNavController()
    NavHost(navController, startDestination = "login") { //ruta login
        composable("login") {
            LoginPage(
                onRegiserClick = {
                    navController.navigate("register") //cand se apeleaza din LoginPage
                },
                onLoginClick = {
                    navController.navigate("homepage")
                }
            )
        }
        composable("register") { //ruta register
            RegisterPage(
                onLoginClick = {
                    navController.popBackStack()
                },
                onRegisterClick = {
                    navController.navigate("homepage")
                }
            ) }
        composable("homepage") {
            HomePage()
        }
    }
}