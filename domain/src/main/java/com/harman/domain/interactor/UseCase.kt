package com.harman.domain.interactor

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

abstract class UseCase<in P, out R>(private val dispatcher: CoroutineDispatcher = Dispatchers.Default) {
    suspend fun execute(param: P): R {
        return withContext(dispatcher) {
            performAction(param)
        }
    }

    protected abstract suspend fun performAction(param: P): R
}