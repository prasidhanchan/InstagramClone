package com.instagramclone.profile

import android.net.Uri
import com.instagramclone.firebase.models.IGUser
import com.instagramclone.util.models.Post

data class UiState(
    var profileImage: String = "",
    var newProfileImage: Uri? = null,
    var username: String = "",
    var name: String = "",
    var email: String = "",
    var phone: String = "",
    var password: String = "",
    var bio: String = "",
    var links: String = "",
    var gender: String = "Unknown",
    var myPosts: List<Post> = emptyList(),
    var followers: List<String> = emptyList(),
    var following: List<String> = emptyList(),
    var isLoading: Boolean = false,
    var isUpdating: Boolean = false,
    var isUserDetailChanged: Boolean = false,
    var error: String? = "",
    var textState: String = "",
    var showPostScreen: Boolean = false,
    var postIndex: Int? = null,
    var passwordState: String = "",
    var newPasswordState: String = "",
    var rePasswordState: String = "",
    var selectedPost: Post = Post(),
    var selectedUserProfile: IGUser = IGUser(),
    var selectedUserPosts: List<Post> = emptyList(),
    var isFollowing: Boolean = false
)
