package com.instagramclone.android.navigation

sealed class NavScreens(
    val name: String = "",
    val route: String = "",
    val icon: Int? = null
) {
    data object SplashScreen: NavScreens(name = "Splash", route = "SplashScreen")
    data object LoginScreen: NavScreens(name = "Login", route = "LoginScreen")
    data object AddEmailScreen: NavScreens(name = "AddEmail", route = "AddEmailScreen")
    data object ChooseUsernameScreen: NavScreens(name = "ChooseUsername", route = "ChooseUsernameScreen")
    data object CreatePasswordScreen: NavScreens(name = "CreatePassword", route = "CreatePasswordScreen")
    data object WelcomeScreen: NavScreens(name = "Welcome", route = "WelcomeScreen")
    data object AddProfileScreen: NavScreens(name = "AddProfile", route = "AddProfileScreen")
    data object ProfileAddedScreen: NavScreens(name = "ProfileAdded", route = "ProfileAddedScreen")
    data object LoginHelpScreen: NavScreens(name = "LoginHelp", route = "LoginHelpScreen")
    data object AccessAccountScreen: NavScreens(name = "AccessAccount", route = "AccessAccountScreen")
    data object HomeScreen: NavScreens(name = "Home", route = "HomeScreen")
}