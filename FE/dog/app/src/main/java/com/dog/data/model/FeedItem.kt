package com.dog.data.model

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.toMutableStateList

data class Comment(
    val id: Long,
    val username: String,
    val text: String
)

data class FeedItem(
    val id: Long,
    val username: String,
    val userProfileImageUrl: String,
    val postImageUrl: List<String>,
    val content: String,
    var likes: MutableState<Long>,
    var comments: Long,
    var isLiked: MutableState<Boolean>,
    var commentsList: MutableState<MutableList<Comment>>,
)


fun generateComments(): List<Comment> {
    return listOf(
        Comment(1, "User1", "Great photo!"),
        Comment(2, "User2", "Love it!"),
        Comment(3, "User3", "Awesome!"),
        Comment(4, "User4", "Amazing!"),
        Comment(5, "User5", "Fantastic!")
    )
}

fun generateFeedItems(): List<FeedItem> {
    val commentsList = generateComments().toMutableStateList()

    return listOf(
        FeedItem(
            1,
            "User123",
            "https://example.com/user123-profile.jpg",
            listOf(
                "https://img.freepik.com/premium-photo/a-cute-little-character-with-a-face-and-a-smile-on-his-face_902049-15707.jpg?w=740",
                "https://img.freepik.com/premium-photo/a-cute-little-character-with-a-face-and-a-smile-on-his-face_902049-15707.jpg?w=740",
                "https://img.freepik.com/premium-photo/a-cute-little-character-with-a-face-and-a-smile-on-his-face_902049-15707.jpg?w=740",
            ),
            "This is an awesome post!",
            mutableStateOf(15),
            10,
            mutableStateOf(false),
            mutableStateOf(commentsList)
        ),
        FeedItem(
            2,
            "User456",
            "https://example.com/user456-profile.jpg",
            listOf(
                "https://example.com/post456-image1.jpg",
                "https://example.com/post456-image2.jpg"
            ),
            "Just shared a photo!",
            mutableStateOf(20),
            8,
            mutableStateOf(true),
            mutableStateOf(commentsList)
        ),
        FeedItem(
            3,
            "User789",
            "https://example.com/user789-profile.jpg",
            listOf("https://example.com/post789-image1.jpg"),
            "Feeling happy today!",
            mutableStateOf(25),
            5,
            mutableStateOf(false),
            mutableStateOf(commentsList)
        ),
        FeedItem(
            4,
            "User101",
            "https://example.com/user101-profile.jpg",
            listOf("https://example.com/post101-image1.jpg"),
            "Amazing view!",
            mutableStateOf(18),
            7,
            mutableStateOf(true),
            mutableStateOf(commentsList)
        )
    )
}
