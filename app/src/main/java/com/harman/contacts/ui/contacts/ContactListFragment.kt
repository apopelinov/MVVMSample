package com.harman.contacts.ui.contacts

import android.app.Activity
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
import com.google.android.material.snackbar.Snackbar
import com.harman.contacts.R
import com.harman.contacts.databinding.ContactListFragmentBinding
import com.harman.contacts.ui.getPluralsWithZero
import com.harman.contacts.vo.State
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

        viewModel.exportedEvent.observe(viewLifecycleOwner, {
            if (it.data is State.Success) {
                Snackbar.make(binding.root, R.string.contacts_exported, Snackbar.LENGTH_SHORT)
                    .show()
            }
        })

        viewModel.importedEvent.observe(viewLifecycleOwner, {
            val data = it.data
            if (data is State.Success) {
                val text = resources.getPluralsWithZero(
                    R.plurals.contacts_imported,
                    R.string.no_contacts_imported,
                    data.value
                )
                Snackbar.make(binding.root, text, Snackbar.LENGTH_SHORT).show()
            }
        })

        setHasOptionsMenu(true)
        return binding.root
    }

    private fun call(phone: String) {
        val number = Uri.parse("tel:$phone")
        val callIntent = Intent(Intent.ACTION_DIAL, number)
        context?.startActivity(callIntent)
    }

    private fun findContactsFile() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        intent.type = MIME_TYPE

        startActivityForResult(intent, READ_REQUEST_CODE)
    }

    private fun createFile() {
        val intent = Intent(Intent.ACTION_CREATE_DOCUMENT)
        intent.addCategory(Intent.CATEGORY_OPENABLE)

        intent.type = MIME_TYPE
        intent.putExtra(Intent.EXTRA_TITLE, EXPORT_FILE_NAME)
        startActivityForResult(intent, WRITE_REQUEST_CODE)
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
            R.id.import_contacts -> findContactsFile()
            R.id.export_contacts -> createFile()
            R.id.add_contact -> findNavController().navigate(ContactListFragmentDirections.editContact())
            R.id.github_users -> findNavController().navigate(ContactListFragmentDirections.viewGithubUsers())
        }
        return true
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode != Activity.RESULT_OK) {
            return super.onActivityResult(requestCode, resultCode, data)
        }
        if (requestCode == READ_REQUEST_CODE) {
            data?.data?.let {
                viewModel.importContacts(it)
            }
        }
        if (requestCode == WRITE_REQUEST_CODE) {
            data?.data?.let {
                viewModel.exportContacts(it)
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    companion object {
        private const val READ_REQUEST_CODE = 1
        private const val WRITE_REQUEST_CODE = 2
        private const val MIME_TYPE = "application/json"
        private const val EXPORT_FILE_NAME = "contacts.json"
    }
}
