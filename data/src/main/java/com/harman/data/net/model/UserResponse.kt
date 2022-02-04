package com.harman.data.net.model

import com.google.gson.annotations.SerializedName

data class UserResponse(
    val login: String,
    val id: Long,
    @SerializedName("avatar_url")
    val avatarUrl: String,
    val url: String,
    @SerializedName("html_url")
    val htmlUrl: String
)