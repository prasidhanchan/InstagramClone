package com.instagramclone.util.constants

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.instagramclone.util.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

/**
 * Function to login using facebook
 * @param loginManager Requires [LoginManager]
 * @param callBack requires [CallbackManager]
 * @param context Requires current Context
 * @param onSuccess on Success lambda triggered when login is successful
 * @param onError on Error lambda triggered when a login error occurs
 */
@Composable
fun FacebookLogin(
    loginManager: LoginManager,
    callBack: CallbackManager,
    context: Context,
    onSuccess: () -> Unit,
    onError: (Exception) -> Unit
) {
    DisposableEffect(key1 = Unit) {
        loginManager.registerCallback(
            callBack,
            object: FacebookCallback<LoginResult> {
                override fun onSuccess(result: LoginResult) {
                    CoroutineScope(Dispatchers.IO).launch {
                        val token = result.accessToken.token
                        val credential = FacebookAuthProvider.getCredential(token)
                        val signInResult = FirebaseAuth.getInstance().signInWithCredential(credential).await()

                            if (signInResult != null) {
                                withContext(Dispatchers.Main) {
                                    onSuccess()
                                }
                            } else {
                                withContext(Dispatchers.Main) {
                                onError(Exception(context.getString(R.string.internal_error)))
                            }
                        }
                    }
                }

                override fun onCancel() {  }

                override fun onError(error: FacebookException) {
                    onError(error)
                }
            }
        )

        onDispose {
            loginManager.unregisterCallback(callBack)
        }
    }
}

/** Function to convert a normal display name to IG username which includes underscores and lowercase letters
 * @return Returns a String converted to username formatted to UG username
 */
fun String?.toIGUsername(): String? = this?.lowercase()?.replace(oldValue = " ", newValue = "_")


/**
 * Function to check if the entered passwords match the current password and new passwords match the password criteria
 * @param currentPassword Current active password
 * @param passwordState Entered current password
 * @param newPasswordState New password password
 * @param rePasswordState New password re-typed
 * @param onSuccess On Success lambda triggered when entered passwords are correct
 * @param onError On Error lambda triggered when there is an error
 */
fun checkPassword(
    currentPassword: String,
    passwordState: String,
    newPasswordState: String,
    rePasswordState: String,
    onSuccess: () -> Unit,
    onError: (String) -> Unit
) {
    if (passwordState == currentPassword && currentPassword.isNotEmpty()) {
        if (newPasswordState == rePasswordState) {
            if (newPasswordState.length > 6) {
                onSuccess()
            } else {
                onError("For security, your password must be 6 characters or more.")
            }
        } else {
            onError("New password does not match. Enter new password again.")
        }
    } else {
        onError("Current Password is incorrect.")
    }
}
