package com.harman.contacts.ui.editcontact

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.harman.contacts.R
import com.harman.contacts.databinding.EditContactFragmentBinding
import com.harman.contacts.ui.Validators
import com.harman.contacts.vo.State
import org.koin.androidx.viewmodel.ext.android.viewModel
import javax.inject.Inject

class EditContactFragment : Fragment() {

    private val viewModel: EditContactViewModel by viewModel()

    private val params by navArgs<EditContactFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel.setContact(params.contactId)
        val binding = EditContactFragmentBinding.inflate(inflater)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        viewModel.firstnameError.observe(viewLifecycleOwner, {
            binding.firstnameEditText.error =
                if (it == null) null else resources.getString(R.string.required_error)
        })
        viewModel.phoneError.observe(viewLifecycleOwner, {
            binding.phoneEditText.error = getError(it, R.string.phone_error)
        })
        viewModel.emailError.observe(viewLifecycleOwner, {
            binding.emailEditText.error = getError(it, R.string.email_error)
        })
        viewModel.savedEvent.observe(viewLifecycleOwner, {
            val data = it.data
            if(data is State.Success) {
                findNavController().navigate(EditContactFragmentDirections.viewContact(data.value))
            }
        })
        binding.cancelButton.setOnClickListener {
            findNavController().popBackStack()
        }

        setHasOptionsMenu(true)
        return binding.root
    }

    private fun getError(error: Validators.ValidationError?, @StringRes patternErrorRestId: Int): String? {
        return when (error) {
            Validators.ValidationError.REQUIRED -> resources.getString(R.string.required_error)
            Validators.ValidationError.PATTERN -> resources.getString(patternErrorRestId)
            else -> null
        }
    }
}
