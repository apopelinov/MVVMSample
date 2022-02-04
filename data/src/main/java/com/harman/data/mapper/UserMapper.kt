package com.harman.data.mapper

import com.harman.data.net.model.UserResponse
import com.harman.domain.model.User

fun List<UserResponse>.toDomainUsers(): List<User> = map {
    User(
        login = it.login,
        id = it.id.toString(),
        avatarUrl = it.avatarUrl,
        url = it.url,
        htmlUrl = it.htmlUrl
    )
}