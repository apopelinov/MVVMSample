package com.harman.contacts.ui.viewcontact

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.harman.contacts.databinding.ViewContactFragmentBinding
import com.harman.contacts.vo.State
import org.koin.androidx.viewmodel.ext.android.viewModel
import javax.inject.Inject

class ViewContactFragment : Fragment() {

    private val viewModel: ViewContactViewModel by viewModel()

    private val params by navArgs<ViewContactFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel.setContact(params.contactId)
        val binding = ViewContactFragmentBinding.inflate(inflater)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        viewModel.contact.observe(viewLifecycleOwner, {
            if (it == null) {
                findNavController().navigate(ViewContactFragmentDirections.editContact())
            }
        })

        viewModel.deletedEvent.observe(viewLifecycleOwner, {
            if(it.data is State.Success) {
                findNavController().popBackStack()
            }
        })

        binding.editButton.setOnClickListener {
            findNavController().navigate(ViewContactFragmentDirections.editContact(params.contactId))
        }

        binding.phoneImageView.setOnClickListener {
            call()
        }

        setHasOptionsMenu(true)
        return binding.root
    }

    private fun call() {
        viewModel.contact.value?.phone?.let {
            val number = Uri.parse("tel:$it")
            val callIntent = Intent(Intent.ACTION_DIAL, number)
            context?.startActivity(callIntent)
        }
    }

}
