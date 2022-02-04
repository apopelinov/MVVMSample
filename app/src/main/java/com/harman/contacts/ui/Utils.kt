package com.harman.contacts.ui

import android.content.res.Resources
import androidx.annotation.PluralsRes
import androidx.annotation.StringRes

fun Resources.getPluralsWithZero(
    @PluralsRes resId: Int, @StringRes zeroResId: Int, quantity: Int,
    vararg formatArgs: Any?
): String {
    return if (quantity == 0) {
        getString(zeroResId)
    } else {
        getQuantityString(resId, quantity, *formatArgs)
    }
}

fun Resources.getPluralsWithZero(
    @PluralsRes resId: Int, @StringRes zeroResId: Int, quantity: Int
): String = getPluralsWithZero(resId, zeroResId, quantity, quantity)