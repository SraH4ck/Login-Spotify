package com.lpm.ej09_login.navigation

import SplashScreen
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.lpm.ej09_login.screen.home.HomeScreen
import com.lpm.ej09_login.screen.login.LoginScreen
import com.lpm.ej09_login.screen.register.RegisterScreen

@Composable
fun Navigation(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Screens.SplashScreen.name) {
        composable(Screens.SplashScreen.name) { SplashScreen(navController) }
        composable(Screens.LoginScreen.name) { LoginScreen(navController) }
        composable(Screens.RegisterScreen.name) { RegisterScreen(navController) }
        composable(Screens.HomeScreen.name) { HomeScreen(navController) }
    }
}