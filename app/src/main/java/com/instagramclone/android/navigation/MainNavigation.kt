package com.instagramclone.android.navigation

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
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
import com.instagramclone.firebase.models.IGUser
import com.instagramclone.home.HomeViewModel
import com.instagramclone.ui.R
import com.instagramclone.util.constants.FacebookLogin
import com.instagramclone.util.constants.toIGUsername
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun MainNavigation(viewModel: AuthViewModel = hiltViewModel()) {
    val navController = rememberNavController()
    val context = LocalContext.current
    var startDestination by remember { mutableStateOf(NavScreens.SplashScreen.route) }

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(NavScreens.SplashScreen.route) {
            val currentUser = FirebaseAuth.getInstance().currentUser

            LaunchedEffect(key1 = true) {
                if (currentUser != null) {
                    startDestination = NavScreens.InnerScreenHolder.route
                    delay(1000)
                    navController.popBackStack()
                    navController.navigate(NavScreens.InnerScreenHolder.route)
                } else {
                    startDestination = NavScreens.LoginScreen.route
                    delay(1000)
                    navController.popBackStack()
                    navController.navigate(NavScreens.LoginScreen.route)
                }
            }
            SplashScreen()
        }
        composable(NavScreens.LoginScreen.route) {
            val uiState by viewModel.uiState.collectAsState()
            val emailList by viewModel.emailList.collectAsState()

            val loginManager = LoginManager.getInstance()
            val callBack = remember { CallbackManager.Factory.create() }
            val launcher = rememberLauncherForActivityResult(
                contract = loginManager.createLogInActivityResultContract(callBack),
                onResult = {  }
            )

            FacebookLogin(
                loginManager = loginManager,
                callBack = callBack,
                context = context,
                onSuccess = {
                    val currentUser = FirebaseAuth.getInstance().currentUser
                    val displayName = currentUser?.displayName?.toIGUsername() //Prasidh Anchan -> prasidh_anchan

                    if (emailList?.contains(currentUser?.email) == true) {
                        navController.popBackStack()
                        navController.navigate(NavScreens.InnerScreenHolder.route) {
                            navController.graph.startDestinationRoute?.let { it1 -> popUpTo(it1) }
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
                                navController.navigate(NavScreens.InnerScreenHolder.route) {
                                    navController.graph.startDestinationRoute?.let { it1 -> popUpTo(it1) }
                                }
                                viewModel.clearUiState()
                            }
                        )
                    }
                },
                onError = { viewModel.setErrorOrSuccess(errorOrSuccess = it.message.toString()) }
            )

            LoginScreen(
                uiState = uiState,
                navigateToSignUp = { navController.navigate(NavScreens.AddEmailScreen.route) },
                onForgotPasswordClicked = { navController.navigate(NavScreens.LoginHelpScreen.route) },
                onFaceBookClicked = { launcher.launch(listOf("email", "public_profile")) },
                onEmailOrUserNameChange = { viewModel.setEmailOrUsername(emailOrUsername = it) },
                onPasswordChange = { viewModel.setPassword(password = it) },
                onConfirm = {
                    viewModel.setDialog(value = false)
                    navController.navigate(NavScreens.AddEmailScreen.route)
                },
                onDismiss = { viewModel.setDialog(value = false) },
                onLogin = {
                    if (uiState.emailOrUsername.matches(Regex("^[a-zA-Z0-9_.]+|[a-zA-Z]+\$\n"))) {
                        viewModel.loginWithUsername(
                            username = uiState.emailOrUsername,
                            password = uiState.password,
                            context = context,
                            onSuccess = {
                                navController.navigate(NavScreens.InnerScreenHolder.route) {
                                    navController.graph.startDestinationRoute?.let { route -> popUpTo(route) }
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
                                navController.navigate(NavScreens.InnerScreenHolder.route) {
                                    navController.graph.startDestinationRoute?.let { route -> popUpTo(route) }
                                }
                                viewModel.clearUiState()
                            }
                        )
                    }
                }
            )
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
                            navController.navigate(NavScreens.CreatePasswordScreen.route)
                        }
                    } else {
                        viewModel.setErrorOrSuccessUsername(
                            errorOrSuccessUsername = context.getString(R.string.username_format_incorrect)
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
                    navController.navigate(NavScreens.InnerScreenHolder.route) {
                        navController.graph.startDestinationRoute?.let { route -> popUpTo(route) }
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
                                    navController.navigate(NavScreens.InnerScreenHolder.route) {
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
        composable(NavScreens.LoginHelpScreen.route) {
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
                    val displayName = currentUser?.displayName?.toIGUsername() //Prasidh Anchan -> prasidh_anchan

                    if (emailList?.contains(currentUser?.email) == true) {
                        navController.popBackStack()
                        navController.navigate(NavScreens.InnerScreenHolder.route)
                        viewModel.clearUiState()
                    } else {
                        viewModel.addUserToDB(
                            igUser = IGUser(
                                email = currentUser?.email!!,
                                password = context.getString(R.string.facebook_login),
                                username = displayName!!
                            ),
                            onSuccess = {
                                navController.navigate(NavScreens.InnerScreenHolder.route) {
                                    navController.graph.startDestinationRoute?.let { route -> popUpTo(route)
                                    }
                                }
                                viewModel.clearUiState()
                            }
                        )
                    }
                },
                onError = {
                    viewModel.setErrorOrSuccess(errorOrSuccess = it.message.toString()) },
                onNextClicked = {
                    viewModel.filterUser(
                        onSuccess = {
                            navController.navigate(NavScreens.AccessAccountScreen.route)
                            viewModel.clearErrorOrSuccess()
                        },
                        onError = { viewModel.setErrorOrSuccess(errorOrSuccess = context.getString(R.string.no_user_found)) }
                    )
                }
            )
        }
        composable(NavScreens.AccessAccountScreen.route) {
            val uiState by viewModel.uiState.collectAsState()

            AccessAccountScreen(
                uiState = uiState,
                setDialog = { viewModel.setDialog(value = it) },
                popBack = { navController.popBackStack() },
                onSendEmailClicked = { viewModel.sendPasswordResetEmail(email = uiState.email) }
            )
        }

        composable(NavScreens.InnerScreenHolder.route) {
            startDestination = NavScreens.InnerScreenHolder.route
            val viewModelHome: HomeViewModel = hiltViewModel()
            val uiState by viewModelHome.uiState.collectAsState()

            InnerScreenHolder(
                viewModel = viewModelHome,
                profileImage = uiState.profileImage
            )
        }
    }
}