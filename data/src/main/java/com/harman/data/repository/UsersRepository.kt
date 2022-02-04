package com.harman.data.repository

import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.harman.data.net.GithubUsersAPI
import com.harman.domain.model.User
import com.harman.domain.repository.UsersRepository

class UsersRepository constructor(private val githubUsersAPI: GithubUsersAPI) : UsersRepository {
    override suspend fun getUsers(lastId: Long?): LiveData<PagingData<User>> {

        val pagingSource = UsersPagingSource(githubUsersAPI)

        return Pager(
            config = createPagingConfig(),
            pagingSourceFactory = { pagingSource }
        ).liveData
    }

    private fun createPagingConfig() =
        PagingConfig(pageSize = 1, enablePlaceholders = false)


    override fun clear() {
    }
}