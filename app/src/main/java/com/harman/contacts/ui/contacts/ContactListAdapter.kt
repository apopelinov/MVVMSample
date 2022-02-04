package com.harman.contacts.ui.contacts

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.harman.contacts.databinding.ContactListItemBinding
import com.harman.domain.model.Contact

class ContactListAdapter(private val listener: ContactListener) :
    PagingDataAdapter<Contact, ContactListAdapter.ViewHolder>(DiffCallback) {
    class ViewHolder(
        private var binding: ContactListItemBinding,
        private val listener: ContactListener
    ) :
        RecyclerView.ViewHolder(binding.root) {
        var contact: Contact? = null

        init {
            binding.phoneImageView.setOnClickListener {
                contact?.let { listener.call(it.phone) }
            }
            binding.root.setOnClickListener {
                contact?.let { listener.open(it.id) }
            }
        }

        fun bind(contact: Contact?) {
            this.contact = contact
            binding.contact = contact
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(
            ContactListItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ), listener
        )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        holder.bind(getItem(position))

    companion object DiffCallback : DiffUtil.ItemCallback<Contact>() {
        override fun areItemsTheSame(oldItem: Contact, newItem: Contact) = oldItem.id === newItem.id

        override fun areContentsTheSame(oldItem: Contact, newItem: Contact) = oldItem == newItem
    }
}