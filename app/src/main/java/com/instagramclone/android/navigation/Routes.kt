package com.instagramclone.android.navigation

import com.instagramclone.ui.R

sealed class Routes(
    val name: String = "",
    val route: String = "",
    val iconOutlined: Int? = null,
    val iconFilled: Int? = null
) {
    data object SplashScreen: Routes(name = "Splash", route = "SplashScreen")
    data object LoginScreen: Routes(name = "Login", route = "LoginScreen")
    data object AddEmailScreen: Routes(name = "AddEmail", route = "AddEmailScreen")
    data object ChooseUsernameScreen: Routes(name = "ChooseUsername", route = "ChooseUsernameScreen")
    data object CreatePasswordScreen: Routes(name = "CreatePassword", route = "CreatePasswordScreen")
    data object WelcomeScreen: Routes(name = "Welcome", route = "WelcomeScreen")
    data object AddProfileScreen: Routes(name = "AddProfile", route = "AddProfileScreen")
    data object ProfileAddedScreen: Routes(name = "ProfileAdded", route = "ProfileAddedScreen")
    data object LoginHelpScreen: Routes(name = "LoginHelp", route = "LoginHelpScreen")
    data object AccessAccountScreen: Routes(name = "AccessAccount", route = "AccessAccountScreen")
    data object InnerScreenHolder: Routes(name = "InnerScreen", route = "InnerScreenHolder")
    data object HomeScreen: Routes(iconOutlined = R.drawable.home_outlined, iconFilled = R.drawable.home_filled, name = "Home", route = "HomeScreen")
    data object SearchScreen: Routes(iconOutlined = R.drawable.search, iconFilled = R.drawable.search_selected, name = "Search", route = "SearchScreen")
    data object UploadContentScreen: Routes(iconOutlined = R.drawable.upload_outlined, name = "UploadContent", route = "UploadContentScreen")
    data object ReelsScreen: Routes(iconOutlined = R.drawable.reel_outlined, iconFilled = R.drawable.reel_filled, name = "Reels", route = "ReelsScreen")
    data object MyProfileScreen: Routes(name = "MyProfile", route = "MyProfileScreen")
    data object UserProfileScreen: Routes(name = "UserProfile", route = "UserProfileScreen")
    data object PostsScreen: Routes(name = "Posts", route = "PostsScreen")
    data object EditProfileScreen: Routes(name = "EditProfile", route = "EditProfileScreen")
    data object EditTextScreen: Routes(name = "EditText", route = "EditTextScreen")
    data object SettingsAndPrivacyScreen: Routes(name = "SettingsAndPrivacy", route = "SettingsAndPrivacyScreen")
    data object AddCaptionScreen: Routes(name = "AddCaption", route = "AddCaptionScreen")
    data object AddToStoryScreen: Routes(name = "AddToStory", route = "AddToStoryScreen")

    data object Items {
        val list = listOf(
            HomeScreen,
            SearchScreen,
            UploadContentScreen,
            ReelsScreen,
            MyProfileScreen
        )
    }
}