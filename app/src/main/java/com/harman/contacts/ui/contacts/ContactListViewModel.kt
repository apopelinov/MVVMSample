package com.harman.contacts.ui.contacts

import android.net.Uri
import androidx.lifecycle.*
import androidx.paging.cachedIn
import com.harman.contacts.ui.BaseViewModel
import com.harman.contacts.ui.MutableLiveEvent
import com.harman.contacts.vo.StateEvent
import com.harman.domain.interactor.ExportContacts
import com.harman.domain.interactor.GetContacts
import com.harman.domain.interactor.GetContactsFiltered
import com.harman.domain.interactor.ImportContacts
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

class ContactListViewModel(
    private val getContacts: GetContacts,
    private val getContactsFiltered: GetContactsFiltered,
    private val importContacts: ImportContacts,
    private val exportContacts: ExportContacts
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

    private val _exportedEvent = MutableLiveEvent<StateEvent<Unit>>()
    val exportedEvent: LiveData<StateEvent<Unit>>
        get() = _exportedEvent

    private val _importedEvent = MutableLiveEvent<StateEvent<Int>>()
    val importedEvent: LiveData<StateEvent<Int>>
        get() = _importedEvent

    init {
        setFilter("")
    }

    fun importContacts(uri: Uri) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                _importedEvent.value = StateEvent.success(importContacts.execute(uri))
            } catch (e: Exception) {
                _importedEvent.value = StateEvent.error()
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun setFilter(originalInput: String) {
        val input = originalInput.lowercase(Locale.getDefault()).trim()
        if (input == filter.value) {
            return
        }
        filter.value = input
    }

    fun exportContacts(uri: Uri) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                exportContacts.execute(uri)
                _exportedEvent.value = StateEvent.success(Unit)
            } catch (e: Exception) {
                _exportedEvent.value = StateEvent.error()
            } finally {
                _isLoading.value = false
            }
        }
    }

    companion object {
        private const val PAGE_SIZE = 20
    }
}
