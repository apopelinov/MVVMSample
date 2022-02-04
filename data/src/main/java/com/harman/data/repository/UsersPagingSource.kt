package com.harman.data.repository

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.harman.data.mapper.toDomainUsers
import com.harman.data.net.GithubUsersAPI
import com.harman.domain.model.User
import retrofit2.HttpException
import java.io.IOException


class UsersPagingSource(
    private val api: GithubUsersAPI
) : PagingSource<Long, User>() {


    override suspend fun load(params: LoadParams<Long>): LoadResult<Long, User> {
        val lastUserId = params.key
        return try {
            val response = api.getUsers(
                userId = lastUserId
            )
            val nextKey =
                if (response.isEmpty()) {
                    null
                } else {
                    response.last().id
                }
            LoadResult.Page(
                data = response.toDomainUsers(),
                prevKey = lastUserId,
                nextKey = nextKey
            )
        } catch (exception: IOException) {
            return LoadResult.Error(exception)
        } catch (exception: HttpException) {
            return LoadResult.Error(exception)
        }
    }

    /**
     * The refresh key is used for subsequent calls to PagingSource.Load after the initial load.
     */
    override fun getRefreshKey(state: PagingState<Long, User>): Long? {
        // We need to get the previous key (or next key if previous is null) of the page
        // that was closest to the most recently accessed index.
        // Anchor position is the most recently accessed index.
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }
}