package com.instagramclone.firebase.models

data class IGUser(
    var userId: String = "",
    var username: String = "",
    var email: String = "",
    var password: String = "",
    var profileImage: String = "",
    var bio: String = "",
    var followersList: List<String> = emptyList(),
    var followingsList: List<String> = emptyList()
) {
    fun convertToMap(): HashMap<String, Any> {
        return hashMapOf(
            "userId" to this.userId,
            "username" to this.username,
            "email" to this.email,
            "password" to this.password,
            "profileImage" to this.profileImage,
            "bio" to this.bio,
            "followersList" to this.followersList,
            "followingsList" to this.followingsList,
        )
    }
}
