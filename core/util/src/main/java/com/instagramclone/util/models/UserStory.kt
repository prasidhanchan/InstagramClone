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

data class UserStoryWithMap(
    var userId: String = "",
    var username: String = "",
    var profileImage: String = "",
    var stories: Map<String, Story> = hashMapOf()
) {
    fun convertToMap(): Map<String, Any> {
        return hashMapOf(
            "userId" to this.userId,
            "username" to this.username,
            "profileImage" to this.profileImage,
            "stories" to this.stories
        )
    }

    fun toUserStory(): UserStory {
        return UserStory(
            userId = this.userId,
            username = this.username,
            profileImage = this.profileImage,
            stories = this.stories.values.toList()
        )
    }
}

fun List<Story>.convertStoriesToMap(): List<Map<String, Story>> {
    return this.map { story ->
        hashMapOf("story-${story.timeStamp}" to story)
    }
}

fun List<Story>.convertListToMap(): Map<String, Story> {
    return this.associateBy { story -> "story-${story.timeStamp}" }
}

fun Story.convertStoryToMap(): Map<String, Story> {
    return hashMapOf("story-${this.timeStamp}" to this)
}