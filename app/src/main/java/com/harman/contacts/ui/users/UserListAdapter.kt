package com.harman.contacts.ui.users

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.harman.contacts.databinding.UserListItemBinding
import com.harman.domain.model.User

class UserListAdapter : PagingDataAdapter<User, UserListAdapter.ViewHolder>(DiffCallback) {
    class ViewHolder(
        private var binding: UserListItemBinding,
    ) :
        RecyclerView.ViewHolder(binding.root) {
        var user: User? = null

        fun bind(user: User?) {
            this.user = user
            binding.user = user
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(
            UserListItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        holder.bind(getItem(position))

    companion object DiffCallback : DiffUtil.ItemCallback<User>() {
        override fun areItemsTheSame(oldItem: User, newItem: User) = oldItem.id === newItem.id

        override fun areContentsTheSame(oldItem: User, newItem: User) = oldItem == newItem
    }
}