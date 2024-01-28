package com.instagramclone.auth

import android.net.Uri

data class UiState(
    var emailOrUsername: String = "",
    var email: String = "",
    var username: String = "",
    var password: String = "",
    var profileImage: Uri? = null,
    var showDialog: Boolean = false,
    var isLoading: Boolean = false,
    var errorTitle: String = "",
    var errorSubTitle: String = "",
    /** Global Error or Success */
    var errorOrSuccess: String = "",
    /** Error or Success for Email Screen */
    var errorOrSuccessEmail: String = "",
    /** Error or Success for Username Screen */
    var errorOrSuccessUsername: String = ""
)
