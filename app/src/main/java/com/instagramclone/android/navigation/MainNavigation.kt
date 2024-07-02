package com.instagramclone.android.navigation

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.media3.common.util.UnstableApi
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.facebook.CallbackManager
import com.facebook.login.LoginManager
import com.google.firebase.auth.FirebaseAuth
import com.instagramclone.android.SplashScreen
import com.instagramclone.auth.AuthViewModel
import com.instagramclone.auth.login.LoginScreen
import com.instagramclone.auth.loginhelp.AccessAccountScreen
import com.instagramclone.auth.loginhelp.LoginHelpScreen
import com.instagramclone.auth.signup.AddEmailScreen
import com.instagramclone.auth.signup.AddProfileScreen
import com.instagramclone.auth.signup.ChooseUsernameScreen
import com.instagramclone.auth.signup.CreatePasswordScreen
import com.instagramclone.auth.signup.ProfileAddedScreen
import com.instagramclone.auth.signup.WelcomeScreen
import com.instagramclone.remote.models.IGUser
import com.instagramclone.ui.R
import com.instagramclone.util.constants.FacebookLogin
import com.instagramclone.util.constants.toIGUsername
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@UnstableApi
@Composable
fun MainNavigation(viewModel: AuthViewModel = hiltViewModel()) {
    val navController = rememberNavController()
    val context = LocalContext.current
    var startDestination by rememberSaveable { mutableStateOf(Routes.SplashScreen.route) }

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(
            route = Routes.SplashScreen.route,
            enterTransition = {
                fadeIn(
                    animationSpec = tween(350)
                )
            },
            exitTransition = {
                fadeOut(
                    animationSpec = tween(350)
                )
            }
        ) {
            val currentUser = FirebaseAuth.getInstance().currentUser

            LaunchedEffect(key1 = Unit) {
                if (currentUser != null) {
                    startDestination = Routes.InnerScreenHolder.route
                    delay(1000L)
                    navController.popBackStack()
                    navController.navigate(Routes.InnerScreenHolder.route)
                } else {
                    startDestination = Routes.LoginScreen.route
                    delay(1000L)
                    navController.popBackStack()
                    navController.navigate(Routes.LoginScreen.route)
                }
            }
            SplashScreen()
        }

        composable(
            route = Routes.LoginScreen.route,
            enterTransition = {
                fadeIn(
                    animationSpec = tween(350)
                )
            },
            exitTransition = {
                fadeOut(
                    animationSpec = tween(350)
                )
            }
        ) {
            val uiState by viewModel.uiState.collectAsState()
            val emailList by viewModel.emailList.collectAsState()

            val loginManager = LoginManager.getInstance()
            val callBack = remember { CallbackManager.Factory.create() }
            val launcher = rememberLauncherForActivityResult(
                contract = loginManager.createLogInActivityResultContract(callBack),
                onResult = { }
            )

            FacebookLogin(
                loginManager = loginManager,
                callBack = callBack,
                context = context,
                onSuccess = {
                    val currentUser = FirebaseAuth.getInstance().currentUser
                    val displayName =
                        currentUser?.displayName?.toIGUsername() //Prasidh Anchan -> prasidh_anchan

                    if (emailList?.contains(currentUser?.email) == true) {
                        navController.popBackStack()
                        navController.navigate(Routes.InnerScreenHolder.route) {
                            navController.graph.startDestinationRoute?.let { route ->
                                popUpTo(route)
                            }
                        }
                        viewModel.clearUiState()
                    } else {
                        viewModel.addUserToDB(
                            igUser = IGUser(
                                username = displayName!!,
                                email = currentUser.email!!,
                                password = context.getString(R.string.facebook_login)
                            ),
                            onSuccess = {
                                navController.popBackStack()
                                navController.navigate(Routes.InnerScreenHolder.route) {
                                    navController.graph.startDestinationRoute?.let { route ->
                                        popUpTo(route)
                                    }
                                }
                                viewModel.clearUiState()
                            }
                        )
                    }
                },
                onError = {
                    viewModel.setErrorOrSuccess(errorOrSuccess = it.message.toString())
                }
            )

            LaunchedEffect(key1 = Unit) {
                startDestination = Routes.LoginScreen.route
            }

            LoginScreen(
                uiState = uiState,
                navigateToSignUp = { navController.navigate(Routes.AddEmailScreen.route) },
                onForgotPasswordClicked = { navController.navigate(Routes.LoginHelpScreen.route) },
                onFaceBookClicked = { launcher.launch(listOf("email", "public_profile")) },
                onEmailOrUserNameChange = { viewModel.setEmailOrUsername(emailOrUsername = it) },
                onPasswordChange = { viewModel.setPassword(password = it) },
                onConfirm = {
                    viewModel.setDialog(value = false)
                    navController.navigate(Routes.AddEmailScreen.route)
                },
                onDismiss = { viewModel.setDialog(value = false) },
                onLogin = {
                    if (uiState.emailOrUsername.matches(Regex("^[a-zA-Z0-9_.]+|[a-zA-Z]+\$\n"))) {
                        viewModel.loginWithUsername(
                            username = uiState.emailOrUsername,
                            password = uiState.password,
                            context = context,
                            onSuccess = {
                                navController.popBackStack()
                                navController.navigate(Routes.InnerScreenHolder.route) {
                                    navController.graph.startDestinationRoute?.let { route ->
                                        popUpTo(route)
                                    }
                                }
                                viewModel.clearUiState()
                            }
                        )
                    } else {
                        viewModel.loginUser(
                            email = uiState.emailOrUsername.trim(),
                            password = uiState.password.trim(),
                            context = context,
                            onSuccess = {
                                navController.popBackStack()
                                navController.navigate(Routes.InnerScreenHolder.route) {
                                    navController.graph.startDestinationRoute?.let { route ->
                                        popUpTo(route)
                                    }
                                }
                                viewModel.clearUiState()
                            }
                        )
                    }
                }
            )
        }

        composable(
            route = Routes.AddEmailScreen.route,
            enterTransition = {
                fadeIn(
                    animationSpec = tween(350)
                )
            },
            exitTransition = {
                fadeOut(
                    animationSpec = tween(350)
                )
            }
        ) {
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
                navigateToLogin = { navController.navigate(Routes.LoginScreen.route) },
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
                            navController.navigate(Routes.ChooseUsernameScreen.route)
                        }
                    } else {
                        viewModel.setErrorOrSuccessEmail(errorOrSuccessEmail = context.getString(R.string.please_enter_a_valid_email))
                    }
                }
            )
        }

        composable(
            route = Routes.ChooseUsernameScreen.route,
            enterTransition = {
                fadeIn(
                    animationSpec = tween(350)
                )
            },
            exitTransition = {
                fadeOut(
                    animationSpec = tween(350)
                )
            }
        ) {
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
                                errorOrSuccessUsername = context.getString(R.string.username_format_incorrect)
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
                            navController.navigate(Routes.CreatePasswordScreen.route)
                        }
                    } else {
                        viewModel.setErrorOrSuccessUsername(
                            errorOrSuccessUsername = context.getString(R.string.username_format_incorrect)
                        )
                    }
                }
            )
        }

        composable(
            route = Routes.CreatePasswordScreen.route,
            enterTransition = {
                fadeIn(
                    animationSpec = tween(350)
                )
            },
            exitTransition = {
                fadeOut(
                    animationSpec = tween(350)
                )
            }
        ) {
            val uiState by viewModel.uiState.collectAsState()

            CreatePasswordScreen(
                uiState = uiState,
                onPasswordChange = {
                    viewModel.setPassword(password = it)
                    if (uiState.errorOrSuccess != "") viewModel.clearErrorOrSuccess()
                },
                onNextClicked = {
                    navController.navigate(Routes.WelcomeScreen.route)
                    viewModel.clearErrorOrSuccess()
                }
            )
        }

        composable(
            route = Routes.WelcomeScreen.route,
            enterTransition = {
                fadeIn(
                    animationSpec = tween(350)
                )
            },
            exitTransition = {
                fadeOut(
                    animationSpec = tween(350)
                )
            }
        ) {
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
                                    navController.navigate(Routes.AddProfileScreen.route)
                                }
                            )
                        }
                    )
                }
            )
        }

        composable(
            route = Routes.AddProfileScreen.route,
            enterTransition = {
                fadeIn(
                    animationSpec = tween(350)
                )
            },
            exitTransition = {
                fadeOut(
                    animationSpec = tween(350)
                )
            }
        ) {
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
                        navController.navigate(Routes.ProfileAddedScreen.route)
                    }
                },
                onSkipClicked = {
                    navController.navigate(Routes.InnerScreenHolder.route) {
                        navController.graph.startDestinationRoute?.let { route -> popUpTo(route) }
                    }
                    viewModel.clearUiState()
                }
            )
        }

        composable(
            route = Routes.ProfileAddedScreen.route,
            enterTransition = {
                fadeIn(
                    animationSpec = tween(350)
                )
            },
            exitTransition = {
                fadeOut(
                    animationSpec = tween(350)
                )
            }
        ) {
            val uiState by viewModel.uiState.collectAsState()

            ProfileAddedScreen(
                uiState = uiState,
                navigateToHomeScreen = {
                    viewModel.convertToUrl(
                        uri = uiState.profileImage,
                        onSuccess = {
                            viewModel.updateProfileImage(
                                onSuccess = {
                                    navController.navigate(Routes.InnerScreenHolder.route) {
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

        composable(
            route = Routes.LoginHelpScreen.route,
            enterTransition = {
                fadeIn(
                    animationSpec = tween(350)
                )
            },
            exitTransition = {
                fadeOut(
                    animationSpec = tween(350)
                )
            }
        ) {
            val uiState by viewModel.uiState.collectAsState()
            val emailList by viewModel.emailList.collectAsState()
            LaunchedEffect(key1 = Unit) {
                viewModel.getAllUsers()
            }

            LoginHelpScreen(
                uiState = uiState,
                onValueChange = { viewModel.setEmailOrUsername(emailOrUsername = it) },
                onSuccess = {
                    val currentUser = FirebaseAuth.getInstance().currentUser
                    val displayName =
                        currentUser?.displayName?.toIGUsername() //Prasidh Anchan -> prasidh_anchan

                    if (emailList?.contains(currentUser?.email) == true) {
                        navController.popBackStack()
                        navController.navigate(Routes.InnerScreenHolder.route)
                        viewModel.clearUiState()
                    } else {
                        viewModel.addUserToDB(
                            igUser = IGUser(
                                email = currentUser?.email!!,
                                password = context.getString(R.string.facebook_login),
                                username = displayName!!
                            ),
                            onSuccess = {
                                navController.navigate(Routes.InnerScreenHolder.route) {
                                    navController.graph.startDestinationRoute?.let { route ->
                                        popUpTo(route)
                                    }
                                }
                                viewModel.clearUiState()
                            }
                        )
                    }
                },
                onError = {
                    viewModel.setErrorOrSuccess(errorOrSuccess = it.message.toString())
                },
                onNextClicked = {
                    viewModel.filterUser(
                        onSuccess = {
                            navController.navigate(Routes.AccessAccountScreen.route)
                            viewModel.clearErrorOrSuccess()
                        },
                        onError = { viewModel.setErrorOrSuccess(errorOrSuccess = context.getString(R.string.no_user_found)) }
                    )
                }
            )
        }

        composable(
            route = Routes.AccessAccountScreen.route,
            enterTransition = {
                fadeIn(
                    animationSpec = tween(350)
                )
            },
            exitTransition = {
                fadeOut(
                    animationSpec = tween(350)
                )
            }
        ) {
            val uiState by viewModel.uiState.collectAsState()

            AccessAccountScreen(
                uiState = uiState,
                setDialog = { viewModel.setDialog(value = it) },
                popBack = { navController.popBackStack() },
                onSendEmailClicked = { viewModel.sendPasswordResetEmail(email = uiState.email) }
            )
        }

        composable(
            route = Routes.InnerScreenHolder.route,
            enterTransition = {
                fadeIn(
                    animationSpec = tween(350)
                )
            },
            exitTransition = {
                fadeOut(
                    animationSpec = tween(350)
                )
            }
        ) {
            LaunchedEffect(key1 = Unit) {
                startDestination = Routes.InnerScreenHolder.route
            }

            InnerScreenHolder(
                navigateToLogin = {
                    navController.popBackStack()
                    navController.clearBackStack(Routes.InnerScreenHolder.route)
                    navController.navigate(Routes.LoginScreen.route)
                }
            )
        }
    }
}