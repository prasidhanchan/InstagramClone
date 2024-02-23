package com.instagramclone.android.navigation

import com.instagramclone.ui.R

sealed class NavScreens(
    val name: String = "",
    val route: String = "",
    val iconOutlined: Int? = null,
    val iconFilled: Int? = null
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
    data object InnerScreenHolder: NavScreens(name = "InnerScreen", route = "InnerScreenHolder")
    data object HomeScreen: NavScreens(iconOutlined = R.drawable.home_outlined, iconFilled = R.drawable.home_filled, name = "Home", route = "HomeScreen")
    data object SearchScreen: NavScreens(iconOutlined = R.drawable.search, iconFilled = R.drawable.search_selected, name = "Search", route = "SearchScreen")
    data object UploadScreen: NavScreens(iconOutlined = R.drawable.upload_outlined, iconFilled = R.drawable.upload_filled, name = "Upload", route = "UploadScreen")
    data object ReelsScreen: NavScreens(iconOutlined = R.drawable.reel_outlined, iconFilled = R.drawable.reel_filled, name = "Reels", route = "ReelsScreen")
    data object ProfileScreen: NavScreens(name = "Profile", route = "ProfileScreen")
    data object EditProfileScreen: NavScreens(name = "EditProfile", route = "EditProfileScreen")
    data object EditTextScreen: NavScreens(name = "EditText", route = "EditTextScreen")
    data object SettingsAndPrivacyScreen: NavScreens(name = "SettingsAndPrivacy", route = "SettingsAndPrivacyScreen")

    data object Items {
        val list = listOf(
            HomeScreen,
            SearchScreen,
            UploadScreen,
            ReelsScreen
        )
    }
}