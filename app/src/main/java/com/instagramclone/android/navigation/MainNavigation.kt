package com.instagramclone.android.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseAuth
import com.instagramclone.android.SplashScreen
import com.instagramclone.auth.AuthViewModel
import com.instagramclone.auth.login.LoginScreen
import com.instagramclone.auth.signup.AddEmailScreen
import com.instagramclone.auth.signup.AddProfileScreen
import com.instagramclone.auth.signup.ChooseUsernameScreen
import com.instagramclone.auth.signup.CreatePasswordScreen
import com.instagramclone.auth.signup.ProfileAddedScreen
import com.instagramclone.auth.signup.WelcomeScreen
import com.instagramclone.firebase.models.IGUser
import com.instagramclone.home.HomeScreen
import com.instagramclone.ui.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun MainNavigation(viewModel: AuthViewModel = hiltViewModel()) {
    val navController = rememberNavController()
    val context = LocalContext.current

    NavHost(
        navController = navController,
        startDestination = NavScreens.SplashScreen.route
    ) {
        composable(NavScreens.SplashScreen.route) {
            val currentUser = FirebaseAuth.getInstance().currentUser

            LaunchedEffect(key1 = true) {
                if (currentUser != null) {
                    delay(1000)
                    navController.popBackStack()
                    navController.navigate(NavScreens.HomeScreen.route)
                } else {
                    delay(1000)
                    navController.popBackStack()
                    navController.navigate(NavScreens.LoginScreen.route)
                }
            }
            SplashScreen()
        }
        composable(NavScreens.LoginScreen.route) {
            val uiState by viewModel.uiState.collectAsState()

            LoginScreen(
                uiState = uiState,
                navigateToSignUp = { navController.navigate(NavScreens.AddEmailScreen.route) },
                onForgotPasswordClicked = { },
                onFaceBookClicked = { },
                onEmailOrUserNameChange = { viewModel.setEmailOrUsername(emailOrUsername = it) },
                onPasswordChange = { viewModel.setPassword(password = it) },
                onConfirm = {
                    viewModel.setDialog(value = false)
                    navController.navigate(NavScreens.AddEmailScreen.route)
                },
                onDismiss = { viewModel.setDialog(value = false) }
            ) {
                viewModel.loginUser(
                    email = uiState.emailOrUsername.trim(),
                    password = uiState.password.trim(),
                    context = context,
                    onSuccess = {
                        navController.navigate(NavScreens.HomeScreen.route)
                        viewModel.clearUiState()
                    }
                )
            }
        }
        composable(NavScreens.AddEmailScreen.route) {
            val uiState by viewModel.uiState.collectAsState()
            val emailList by viewModel.emailList.collectAsState()
            val scope = rememberCoroutineScope()

            AddEmailScreen(
                uiState = uiState,
                clearEmail = { viewModel.clearEmail() },
                onEmailChange = {
                    viewModel.setEmail(email = it)

                    scope.launch {
                        delay(1000L)
                        if (uiState.email.matches(Regex(pattern = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}\$"))) {
                            viewModel.setErrorOrSuccessEmail(
                                errorOrSuccessEmail = context.getString(
                                    R.string.available
                                )
                            )
                            if (emailList?.contains(uiState.email) == true) {
                                viewModel.setErrorOrSuccessEmail(
                                    errorOrSuccessEmail = context.getString(
                                        R.string.email_already_exits
                                    )
                                )
                            } else {
                                viewModel.setErrorOrSuccessEmail(
                                    errorOrSuccessEmail = context.getString(
                                        R.string.available
                                    )
                                )
                            }
                        } else {
                            viewModel.setErrorOrSuccessEmail(
                                errorOrSuccessEmail = context.getString(
                                    R.string.please_enter_a_valid_email
                                )
                            )
                        }
                    }
                },
                navigateToLogin = { navController.navigate(NavScreens.LoginScreen.route) },
                onNextClicked = {
                    if (uiState.email.matches(Regex(pattern = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}\$"))) {
                        viewModel.setErrorOrSuccessEmail(errorOrSuccessEmail = context.getString(R.string.available))
                        if (emailList?.contains(uiState.email) == true) {
                            viewModel.setErrorOrSuccessEmail(
                                errorOrSuccessEmail = context.getString(
                                    R.string.email_already_exits
                                )
                            )
                        } else {
                            navController.navigate(NavScreens.ChooseUsernameScreen.route)
                        }
                    } else {
                        viewModel.setErrorOrSuccessEmail(errorOrSuccessEmail = context.getString(R.string.please_enter_a_valid_email))
                    }
                }
            )
        }
        composable(NavScreens.ChooseUsernameScreen.route) {
            val uiState by viewModel.uiState.collectAsState()
            val usernameList by viewModel.usernameList.collectAsState()
            val scope = rememberCoroutineScope()

            ChooseUsernameScreen(
                uiState = uiState,
                onUserNameChange = {
                    viewModel.setUsername(username = it)

                    scope.launch {
                        delay(1000L)
                        if (uiState.username.matches(Regex("^[a-zA-Z0-9_.]+|[a-zA-Z]+\$\n"))) {
                            viewModel.setErrorOrSuccessUsername(
                                errorOrSuccessUsername = context.getString(
                                    R.string.available
                                )
                            )
                            if (usernameList?.contains(uiState.username) == true) {
                                viewModel.setErrorOrSuccessUsername(
                                    errorOrSuccessUsername = context.getString(
                                        R.string.username_already_exists
                                    )
                                )
                            } else {
                                viewModel.setErrorOrSuccessUsername(
                                    errorOrSuccessUsername = context.getString(
                                        R.string.available
                                    )
                                )
                            }
                        } else {
                            viewModel.setErrorOrSuccessUsername(
                                errorOrSuccessUsername = context.getString(R.string.usernames_can_only_contain_letter_numbers_underscores_and_periods)
                            )
                        }
                    }
                },
                clearUsername = { viewModel.clearUsername() },
                onNextClicked = {
                    if (uiState.username.matches(Regex("^[a-zA-Z0-9_.]+|[a-zA-Z]+\$\n"))) {
                        viewModel.setErrorOrSuccessUsername(
                            errorOrSuccessUsername = context.getString(
                                R.string.available
                            )
                        )
                        if (usernameList?.contains(uiState.username) == true) {
                            viewModel.setErrorOrSuccessUsername(
                                errorOrSuccessUsername = context.getString(
                                    R.string.username_already_exists
                                )
                            )
                        } else {
                            viewModel.setErrorOrSuccessUsername(
                                errorOrSuccessUsername = context.getString(
                                    R.string.available
                                )
                            )
                            navController.navigate(NavScreens.CreatePasswordScreen.route)
                        }
                    } else {
                        viewModel.setErrorOrSuccessUsername(
                            errorOrSuccessUsername = context.getString(R.string.usernames_can_only_contain_letter_numbers_underscores_and_periods)
                        )
                    }
                }
            )
        }
        composable(NavScreens.CreatePasswordScreen.route) {
            val uiState by viewModel.uiState.collectAsState()

            CreatePasswordScreen(
                uiState = uiState,
                onPasswordChange = {
                    viewModel.setPassword(password = it)
                    if (uiState.errorOrSuccess != "") viewModel.clearErrorOrSuccess()
                },
                onNextClicked = {
                    navController.navigate(NavScreens.WelcomeScreen.route)
                    viewModel.clearErrorOrSuccess()
                }
            )
        }
        composable(NavScreens.WelcomeScreen.route) {
            val uiState by viewModel.uiState.collectAsState()

            WelcomeScreen(
                uiState = uiState,
                onCompleteSignUpClicked = {
                    viewModel.signInUser(
                        email = uiState.email.trim(),
                        password = uiState.password.trim(),
                        onSuccess = {
                            viewModel.addUserToDB(
                                igUser = IGUser(
                                    username = uiState.username.trim(),
                                    email = uiState.email.trim(),
                                    password = uiState.password.trim()
                                ),
                                onSuccess = {
                                    navController.navigate(NavScreens.AddProfileScreen.route)
                                }
                            )
                        }
                    )
                }
            )
        }
        composable(NavScreens.AddProfileScreen.route) {
            val uiState by viewModel.uiState.collectAsState()
            val scope = rememberCoroutineScope()

            AddProfileScreen(
                uiState = uiState,
                setProfileImage = { viewModel.setProfileImage(profileImage = it) },
                setDialog = { viewModel.setDialog(it) },
                navigateToProfileAddedScreen = {
                    scope.launch {
                        viewModel.setDialog(value = false)
                        delay(1000)
                        navController.navigate(NavScreens.ProfileAddedScreen.route)
                    }
                },
                onSkipClicked = {
                    navController.navigate(NavScreens.HomeScreen.route) {
                        popUpTo(navController.graph.startDestinationId)
                    }
                    viewModel.clearUiState()
                }
            )
        }
        composable(NavScreens.ProfileAddedScreen.route) {
            val uiState by viewModel.uiState.collectAsState()

            ProfileAddedScreen(
                uiState = uiState,
                navigateToHomeScreen = {
                    viewModel.convertToUrl(
                        uri = uiState.profileImage,
                        onSuccess = {
                            viewModel.updateProfileImage(
                                onSuccess = {
                                    navController.navigate(NavScreens.HomeScreen.route) {
                                        popUpTo(navController.graph.startDestinationId)
                                    }
                                    viewModel.clearUiState()
                                }
                            )
                        }
                    )
                },
                onChangePhotoClicked = {
                    viewModel.setProfileImage(profileImage = null)
                    navController.popBackStack()
                }
            )
        }

        composable(NavScreens.HomeScreen.route) {
            HomeScreen(
                logOut = {
                    viewModel.logOut()
                    navController.popBackStack()
                    navController.navigate(NavScreens.LoginScreen.route)
                }
            )
        }
    }
}