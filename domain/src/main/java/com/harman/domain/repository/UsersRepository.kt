package com.harman.domain.repository

import androidx.lifecycle.LiveData
import androidx.paging.PagingData
import com.harman.domain.model.User

interface UsersRepository : Repository {
    suspend fun getUsers(lastId: Long?): LiveData<PagingData<User>>
}