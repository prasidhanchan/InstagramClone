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
)
