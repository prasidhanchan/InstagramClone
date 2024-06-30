package com.instagramclone.remote.models

data class IGUser(
    var userId: String = "",
    var username: String = "",
    var name: String = "IGUser",
    var gender: String = "Unknown",
    var email: String = "",
    var phone: String = "",
    var password: String = "",
    var profileImage: String = "",
    var bio: String = "",
    var links: String = "",
    var followersList: List<String> = emptyList(),
    var followingList: List<String> = emptyList()
) {
    fun convertToMap(): HashMap<String, Any> {
        return hashMapOf(
            "userId" to this.userId,
            "username" to this.username,
            "name" to this.name,
            "gender" to this.gender,
            "email" to this.email,
            "password" to this.password,
            "profileImage" to this.profileImage,
            "bio" to this.bio,
            "links" to this.links,
            "followersList" to this.followersList,
            "followingList" to this.followingList,
        )
    }
}
