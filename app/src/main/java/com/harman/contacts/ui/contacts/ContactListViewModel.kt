package com.harman.contacts.ui.contacts

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.harman.contacts.ui.BaseViewModel
import com.harman.domain.interactor.GetContacts
import com.harman.domain.interactor.GetContactsFiltered
import java.util.*

class ContactListViewModel(
    private val getContacts: GetContacts,
    private val getContactsFiltered: GetContactsFiltered
) : BaseViewModel() {

    private val filter = MutableLiveData<String>()

    val contacts = filter.switchMap {
        if (it.isBlank()) {
            liveData { emitSource(getContacts.execute(PAGE_SIZE).cachedIn(viewModelScope)) }
        } else {
            liveData {
                emitSource(
                    getContactsFiltered.execute(GetContactsFiltered.Param(PAGE_SIZE, it))
                        .cachedIn(viewModelScope)
                )
            }
        }
    }

    init {
        setFilter("")
    }

    fun setFilter(originalInput: String) {
        val input = originalInput.lowercase(Locale.getDefault()).trim()
        if (input == filter.value) {
            return
        }
        filter.value = input
    }

    companion object {
        private const val PAGE_SIZE = 20
    }
}
