package com.harman.contacts.ui.users


import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.harman.contacts.ui.BaseViewModel
import com.harman.domain.interactor.GetUsers
import com.harman.domain.model.User


class GithubUsersViewModel constructor(
    private val getUsers: GetUsers
) : BaseViewModel() {

    val users: LiveData<PagingData<User>> =
        liveData {
            emitSource(
                getUsers.execute(null).cachedIn(viewModelScope)
            )
        }
}