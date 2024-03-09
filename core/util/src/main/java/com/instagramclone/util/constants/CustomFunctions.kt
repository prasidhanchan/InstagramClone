package com.instagramclone.util.constants

import android.content.Context
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
                        val signInResult = FirebaseAuth.getInstance()
                            .signInWithCredential(credential)
                            .await()

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

/** Function to format TimeStamp for posts
 * @return Returns a string with the formatted timeStamp for comments i.e "1 second ago, "1 hour ago, etc"
 * */
fun Long.formatTimeStamp(): String {
    val timeDiff = System.currentTimeMillis() - this
    return when {
        timeDiff <= 60_000 -> "${timeDiff / 10_000}${if ((timeDiff / 10_000) <= 1L) "second ago" else "seconds ago"}"
        timeDiff <= 3_600_000 -> "${timeDiff / 60_000}${if ((timeDiff / 60_000) <= 1L) " minute ago" else " minutes ago"}"
        timeDiff <= 86_400_000 -> "${timeDiff / 3_600_000}${if ((timeDiff / 3_600_000) <= 1L) " hour ago" else " hours ago"}"
        else -> "${timeDiff / 86_400_000}${if ((timeDiff / 86_400_000) <= 1L) " day ago" else " days ago"}"
    }
}

