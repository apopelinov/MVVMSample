package com.harman.contacts.ui.editcontact

import androidx.lifecycle.*
import com.harman.contacts.ui.BaseViewModel
import com.harman.contacts.ui.MutableLiveEvent
import com.harman.contacts.ui.Validators
import com.harman.contacts.vo.StateEvent
import com.harman.domain.interactor.GetContactById
import com.harman.domain.interactor.SaveContact
import com.harman.domain.model.Contact
import kotlinx.coroutines.launch

class EditContactViewModel constructor(
    private val getContactById: GetContactById,
    private val saveContact: SaveContact
) : BaseViewModel() {
    var contactId: Long? = null
    val firstname = MutableLiveData<String>()
    val lastname = MutableLiveData<String>()
    val phone = MutableLiveData<String>()
    val email = MutableLiveData<String>()

    val firstnameError = firstname.map { Validators.required(it) }
    val phoneError = phone.map { Validators.phone(it, true) }
    val emailError = email.map { Validators.email(it, true) }
    private val _isFormValid = MediatorLiveData<Boolean>().apply {
        listOf(firstnameError, phoneError, emailError).forEach { source ->
            addSource(source) { this.value = validateForm() }
        }
    }
    val isFormValid: LiveData<Boolean>
        get() = _isFormValid

    private val _savedEvent = MutableLiveEvent<StateEvent<Long>>()
    val savedEvent: LiveData<StateEvent<Long>>
        get() = _savedEvent

    private fun validateForm(): Boolean {
        return Validators.required(firstname.value) == null
                && Validators.phone(phone.value, true) == null
                && Validators.email(email.value, true) == null
    }

    fun setContact(id: Long) {
        if (id > 0) {
            viewModelScope.launch {
                _isLoading.value = true
                try {
                    getContactById.execute(id)?.let {
                        contactId = it.id
                        firstname.value = it.firstname
                        lastname.value = it.lastname
                        phone.value = it.phone
                        email.value = it.email
                    }
                } catch (ex: Exception) {
                } finally {
                    _isLoading.value = false
                }
            }
        }
    }

    fun saveContact() {
        if (isFormValid.value == false) {
            return
        }
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val contact = Contact(
                    contactId,
                    firstname.value ?: "",
                    lastname.value ?: "",
                    phone.value ?: "",
                    email.value ?: ""
                )
                val id = saveContact.execute(contact)
                _savedEvent.value = StateEvent.success(id)
            } catch (ex: Exception) {
                _savedEvent.value = StateEvent.error()
            } finally {
                _isLoading.value = false
            }
        }
    }
}
