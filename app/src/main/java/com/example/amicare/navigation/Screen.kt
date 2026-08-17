package com.example.amicare.navigation

sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object Login : Screen("login")
    object Home : Screen("home")
    object CreateComplaint : Screen("create_complaint")
    object History : Screen("history")
    object Profile : Screen("profile")
    object ComplaintDetail : Screen("complaint_detail")
}