package com.instagramclone.util.constants

import android.content.Context
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.geometry.Offset
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.instagramclone.util.R
import com.instagramclone.util.models.Post
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.util.Locale
import java.util.concurrent.TimeUnit
import kotlin.math.abs

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
            object : FacebookCallback<LoginResult> {
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

                override fun onCancel() {}

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
 * @return Returns a string with the formatted timeStamp for comments, posts i.e "1 second ago, "1 hour ago, etc"
 * */
fun Long.formatTimeStamp(): String {
    val timeDiff = System.currentTimeMillis() - this
    return when {
        timeDiff <= 60_000 -> "${timeDiff / 10_000}${if ((timeDiff / 10_000) <= 1L) " second ago" else " seconds ago"}"
        timeDiff <= 3_600_000 -> "${timeDiff / 60_000}${if ((timeDiff / 60_000) <= 1L) " minute ago" else " minutes ago"}"
        timeDiff <= 86_400_000 -> "${timeDiff / 3_600_000}${if ((timeDiff / 3_600_000) <= 1L) " hour ago" else " hours ago"}"
        else -> "${timeDiff / 86_400_000}${if ((timeDiff / 86_400_000) <= 1L) " day ago" else " days ago"}"
    }
}

/** Function to format TimeStamp for stories
 * @return Returns a string with the formatted timeStamp for uploaded stories i.e "1m, "1h, etc"
 * */
fun Long.formatToStoryTimeStamp(): String {
    val timeDiff = System.currentTimeMillis() - this
    return when {
        timeDiff <= 60_000 -> "${timeDiff / 10_000}s"
        timeDiff <= 3_600_000 -> "${timeDiff / 60_000}m"
        timeDiff <= 86_400_000 -> "${timeDiff / 3_600_000}h"
        else -> "${timeDiff / 86_400_000}d"
    }
}

/**
 * Function to format Long video duration to minutes and seconds ex: 10:05
 * @return Returns the formatted timestamp as a String
 */
fun Long.formatMinSec(): String {
    return if (this == 0L) {
        "00:00"
    } else {
        String.format(
            Locale.getDefault(),
            "%02d:%02d",
            TimeUnit.MILLISECONDS.toMinutes(this),
            TimeUnit.MILLISECONDS.toSeconds(this) -
                    TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(this))
        )
    }
}

/**
 * Function to get video [Post] to play in the view
 * Retrieves a single video from the list which is nearest to the midpoint
 * @param state Requires [LazyListState]
 * @param posts Requires a List of [Post]
 * @return Returns a [Post] if the it contains a video else null
 */
fun getCurrentlyPlayingPost(
    state: LazyListState,
    posts: List<Post>
): Post? {
    val layoutInfo = state.layoutInfo

    val midpoint = (layoutInfo.viewportSize.height) / 2
    val centerItems = layoutInfo.visibleItemsInfo
        .sortedBy { abs((it.offset + it.size) / 2 - midpoint) }

    val result = centerItems.map { posts[(it.index - 1).coerceAtLeast(0)] }.firstOrNull()

    return if (result?.mimeType?.contains("video") == true) result else null
}

/**
 * Function to get dynamic alpha on drag for the background.
 * @param offsetY Requires the [Offset.y] dragged.
 * @return Returns the alpha value as per the offsetY
 */
fun getAlphaForBackground(offsetY: Float): Float {
    return when(offsetY) {
        in -5f..10f -> 1f

        in 11f..100f -> 0.8f

        in 101f..500f -> 0.5f

        else -> 0f
    }
}