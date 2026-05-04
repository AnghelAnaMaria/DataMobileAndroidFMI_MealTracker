package cst.unibucfmi2026.ui.navigation

import android.app.Activity
import android.content.Intent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import cst.unibucfmi2026.MainActivity
import cst.unibucfmi2026.ui.pages.HomePage
import cst.unibucfmi2026.ui.pages.LoginPage
import cst.unibucfmi2026.ui.pages.RegisterPage
import cst.unibucfmi2026.viewmodel.AuthViewModel


@Composable
fun AuthNavigation(authViewModel: AuthViewModel = viewModel()) {
    val navController = rememberNavController()
    val authState by authViewModel.authState.collectAsState()
    val navigateToHome: () -> Unit = { navController.navigate("homepage") }
    val context = LocalContext.current
    val startDestination = when (authViewModel.isLoggedIn) {
        true -> "homepage"
        false -> "login"
    }

    NavHost(navController, startDestination = startDestination) {
        composable("login") { //ruta login
            LoginPage( //login page
                onRegisterClick = { //callbacks
                    navController.navigate("register")
                },
                onLoginClick = authViewModel::login,
                onLoginSuccess = navigateToHome,
                isLoading = authState.isLoading,
                errorMessage = authState.errorMessage
            )
        }
        composable("register") { //ruta register
            RegisterPage( //register screen
                onLoginClick = { //callbacks
                    navController.popBackStack()
                },
                onRegisterClick = authViewModel::register,
                onRegisterSuccess = navigateToHome,
                isLoading = authState.isLoading,
                errorMessage = authState.errorMessage
            )
        }
        composable("homepage") {
            HomePage(onLogout = {
                authViewModel.logout()
                (context as? Activity)?.apply{
                    val intent = Intent(this, MainActivity::class.java)
                    this.startActivity(intent)
                    this.finish()
                }
            }
            )
        }
    }
}