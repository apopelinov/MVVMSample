package com.harman.domain.interactor

import androidx.lifecycle.LiveData
import androidx.paging.PagingData
import com.harman.domain.model.User
import com.harman.domain.repository.UsersRepository

class GetUsers constructor(private val usersRepository: UsersRepository) :
    UseCase<Long?, LiveData<PagingData<User>>>() {
    override suspend fun performAction(param: Long?) = usersRepository.getUsers(param)
}