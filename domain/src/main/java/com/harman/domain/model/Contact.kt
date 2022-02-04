package com.harman.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

@Entity
data class Contact(
    @field:Expose
    @PrimaryKey(autoGenerate = true)
    val id: Long? = null,
    @field:SerializedName("firstname")
    val firstname: String,
    @field:SerializedName("lastname")
    val lastname: String,
    @field:SerializedName("phone")
    val phone: String,
    @field:SerializedName("email")
    val email: String
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Contact

        if (firstname != other.firstname) return false
        if (lastname != other.lastname) return false
        if (phone != other.phone) return false
        if (email != other.email) return false

        return true
    }

    override fun hashCode(): Int {
        var result = firstname.hashCode()
        result = 31 * result + lastname.hashCode()
        result = 31 * result + phone.hashCode()
        result = 31 * result + email.hashCode()
        return result
    }
}