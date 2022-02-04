package com.harman.contacts.ui.contacts

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.harman.contacts.databinding.ContactListFooterItemBinding

class ContactLoadStateAdapter : LoadStateAdapter<ContactLoadStateAdapter.ViewHolder>() {
    class ViewHolder(private var binding: ContactListFooterItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(loadState: LoadState) {
            if(loadState is LoadState.Error) {
                binding.errorMsg = loadState.error.localizedMessage
            }
            binding.isLoading = loadState is LoadState.Loading
            binding.executePendingBindings()
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): ViewHolder {
        return ViewHolder(ContactListFooterItemBinding.inflate(LayoutInflater.from(parent.context)))
    }
}