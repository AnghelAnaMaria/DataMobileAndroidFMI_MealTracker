package cst.unibucfmi2026.ui.navigation

import android.app.Activity
import android.content.Intent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import cst.unibucfmi2026.MainActivity
import cst.unibucfmi2026.ui.pages.MyFoodPage
import cst.unibucfmi2026.ui.pages.SearchFoodPage
import cst.unibucfmi2026.ui.pages.HomePage
import cst.unibucfmi2026.ui.pages.LoginPage
import cst.unibucfmi2026.ui.pages.RegisterPage
import cst.unibucfmi2026.viewmodel.AuthViewModel
import cst.unibucfmi2026.viewmodel.FoodViewModel


@Composable
fun AuthNavigation(
    authViewModel: AuthViewModel = viewModel(),
) {
    val navController = rememberNavController()
    val authState by authViewModel.authState.collectAsState()
    val navigateToHome: () -> Unit = { navController.navigate("homepage") }
    val context = LocalContext.current
    val startDestination = when (authViewModel.isLoggedIn) {
        true -> "homepage"
        false -> "login"
    }

    NavHost(navController, startDestination = startDestination) {
        composable("login") {
            LoginPage(
                onRegisterClick = { navController.navigate("register") },
                onLoginClick = authViewModel::login,
                onLoginSuccess = navigateToHome,
                isLoading = authState.isLoading,
                errorMessage = authState.errorMessage
            )
        }
        composable("register") {
            RegisterPage(
                onLoginClick = { navController.popBackStack() },
                onRegisterClick = authViewModel::register,
                onRegisterSuccess = navigateToHome,
                isLoading = authState.isLoading,
                errorMessage = authState.errorMessage
            )
        }
        composable("homepage") {
            HomePage(
                onLogout = {
                    authViewModel.logout()
                    (context as? Activity)?.apply {
                        startActivity(Intent(this, MainActivity::class.java))
                        finish()
                    }
                },
                onTestFoodApi = { navController.navigate("search_food") },
                onMyFoods = {navController.navigate("my_foods")}
            )
        }
        composable("search_food") {
            SearchFoodPage(
                onNavigateToMyFoods = { navController.navigate("my_foods") },
                onGoHome = navigateToHome
            )
        }
        composable("my_foods") {
            MyFoodPage(
                onGoHome = navigateToHome
            )
        }
    }
}
