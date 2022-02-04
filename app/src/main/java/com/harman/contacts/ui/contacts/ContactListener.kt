package com.harman.contacts.ui.contacts

interface ContactListener {
    fun call(phone: String)
    fun open(contactId: Long?)
}