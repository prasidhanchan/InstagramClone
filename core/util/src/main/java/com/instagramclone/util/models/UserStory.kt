package com.instagramclone.util.models

data class UserStory(
    var userId: String = "",
    var username: String = "",
    var profileImage: String = "",
    var stories: List<Story> = emptyList()
) {
    fun convertToMap(): Map<String, Any> {
        return hashMapOf(
            "userId" to this.userId,
            "username" to this.username,
            "profileImage" to this.profileImage,
            "stories" to this.stories
        )
    }
}