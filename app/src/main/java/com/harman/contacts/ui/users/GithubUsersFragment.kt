package com.harman.contacts.ui.users

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import com.harman.contacts.databinding.UsersListFragmentBinding
import com.harman.contacts.ui.contacts.ContactLoadStateAdapter
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel


class GithubUsersFragment : Fragment() {

    private val viewModel: GithubUsersViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = UsersListFragmentBinding.inflate(inflater)
        binding.viewModel = viewModel
        binding.list.addItemDecoration(
            DividerItemDecoration(
                this.context,
                DividerItemDecoration.VERTICAL
            )
        )
        binding.list.adapter = UserListAdapter().apply {
            viewModel.users.observe(viewLifecycleOwner, {
                lifecycleScope.launch { submitData(it) }
            })
        }.withLoadStateFooter(ContactLoadStateAdapter())
        binding.lifecycleOwner = viewLifecycleOwner

        setHasOptionsMenu(true)
        return binding.root
    }

}
