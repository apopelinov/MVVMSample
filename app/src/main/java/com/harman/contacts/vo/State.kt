package com.harman.contacts.vo

sealed class State<out T> {
    object Error: State<Nothing>()
    data class Success<T>(val value: T) : State<T>()
}