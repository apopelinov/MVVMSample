package com.harman.contacts.ui

import android.view.View
import android.widget.EditText
import androidx.annotation.StringRes
import androidx.databinding.BindingAdapter

object BindingAdapters {
    @JvmStatic
    @BindingAdapter("visibleGone")
    fun showHide(view: View, show: Boolean?) {
        view.visibility = if (show == true) View.VISIBLE else View.GONE
    }

    @JvmStatic
    @BindingAdapter("error")
    fun setrError(editText: EditText, @StringRes error: Int) {
        editText.error = editText.resources.getString(error)
    }
}