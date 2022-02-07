package com.harman.contacts.ui.contacts

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.*
import androidx.core.view.iterator
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import com.harman.contacts.R
import com.harman.contacts.databinding.ContactListFragmentBinding
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel


class ContactListFragment : Fragment() {

    private val viewModel: ContactListViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = ContactListFragmentBinding.inflate(inflater)
        binding.viewModel = viewModel
        binding.list.addItemDecoration(
            DividerItemDecoration(
                this.context,
                DividerItemDecoration.VERTICAL
            )
        )
        binding.list.adapter = ContactListAdapter(object : ContactListener {
            override fun call(phone: String) = this@ContactListFragment.call(phone)

            override fun open(contactId: Long?) {
                findNavController().navigate(
                    ContactListFragmentDirections.viewContact(contactId ?: -1)
                )
            }

        }).apply {
            withLoadStateFooter(ContactLoadStateAdapter())
            viewModel.contacts.observe(viewLifecycleOwner, {
                lifecycleScope.launch { submitData(it) }
            })
        }
        binding.lifecycleOwner = viewLifecycleOwner
        binding.filterEditText.addTextChangedListener {
            viewModel.setFilter(it.toString())
        }

        setHasOptionsMenu(true)
        return binding.root
    }

    private fun call(phone: String) {
        val number = Uri.parse("tel:$phone")
        val callIntent = Intent(Intent.ACTION_DIAL, number)
        context?.startActivity(callIntent)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.action_menu, menu)

        viewModel.isLoading.observe(viewLifecycleOwner, { isLoading ->
            menu.iterator().forEach {
                it.isEnabled = !isLoading
            }
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (viewModel.isLoading.value == true) return true
        when (item.itemId) {
            R.id.open_about -> findNavController().navigate(ContactListFragmentDirections.openAbout())
            R.id.add_contact -> findNavController().navigate(ContactListFragmentDirections.editContact())
            R.id.github_users -> findNavController().navigate(ContactListFragmentDirections.viewGithubUsers())
        }
        return true
    }

    companion object {
        private const val READ_REQUEST_CODE = 1
        private const val WRITE_REQUEST_CODE = 2
        private const val MIME_TYPE = "application/json"
        private const val EXPORT_FILE_NAME = "contacts.json"
    }
}
