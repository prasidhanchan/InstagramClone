package com.instagramclone.profile

import android.net.Uri
import com.instagramclone.util.models.Post

data class UiState(
    var profileImage: String = "",
    var newProfileImage: Uri? = null,
    var username: String = "",
    var name: String = "",
    var bio: String = "",
    var links: String = "",
    var gender: String = "Unknown",
    var myPosts: List<Post> = emptyList(),
    var followers: List<String> = emptyList(),
    var following: List<String> = emptyList(),
    var isLoading: Boolean = false,
    var isUpdating: Boolean = false,
    var isUserDetailChanged: Boolean = false,
    var error: String = "",
    var textState: String = "",
    var showPostScreen: Boolean = false,
    var postIndex: Int? = null
)
