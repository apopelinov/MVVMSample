package com.harman.data.net

import com.harman.data.net.model.UserResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface GithubUsersAPI {
    @GET("users")
    suspend fun getUsers(@Query("since") userId: Long?): List<UserResponse>
}