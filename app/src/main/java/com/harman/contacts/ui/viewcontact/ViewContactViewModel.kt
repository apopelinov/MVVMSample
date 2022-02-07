package com.harman.contacts.ui.viewcontact

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.harman.contacts.ui.BaseViewModel
import com.harman.contacts.ui.MutableLiveEvent
import com.harman.contacts.vo.StateEvent
import com.harman.domain.interactor.DeleteContact
import com.harman.domain.interactor.GetContactById
import com.harman.domain.model.Contact
import kotlinx.coroutines.launch

class ViewContactViewModel constructor(
    private val getContactById: GetContactById,
    private val deleteContact: DeleteContact
) : BaseViewModel() {
    private val _contact = MutableLiveData<Contact>()
    val contact: LiveData<Contact>
        get() = _contact

    private val _deletedEvent = MutableLiveEvent<StateEvent<Unit>>()
    val deletedEvent: LiveData<StateEvent<Unit>>
        get() = _deletedEvent

    fun setContact(id: Long) {
        if (id > 0) {
            viewModelScope.launch {
                _isLoading.value = true
                try {
                    _contact.value = getContactById.execute(id)
                } catch (ex: Exception) {
                } finally {
                    _isLoading.value = false
                }
            }
        }
    }

    fun deleteContact() {
        viewModelScope.launch {
            contact.value?.id?.let {
                try {
                    _isLoading.value = true
                    deleteContact.execute(it)
                    _deletedEvent.value = StateEvent.success(Unit)
                } catch (ex: Exception) {
                    _deletedEvent.value = StateEvent.error()
                } finally {
                    _isLoading.value = false
                }
            }
        }
    }
}
