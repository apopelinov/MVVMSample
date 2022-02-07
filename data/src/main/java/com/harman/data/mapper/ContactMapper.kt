package com.harman.data.mapper

import com.harman.data.db.model.DbContact
import com.harman.domain.model.Contact

fun DbContact.toDomainContact(): Contact = Contact(
    id = id,
    firstname = firstname,
    lastname = lastname,
    phone = phone,
    email = email
)

fun Contact.toDbContact(): DbContact = DbContact(
    id = id,
    firstname = firstname,
    lastname = lastname,
    phone = phone,
    email = email
)