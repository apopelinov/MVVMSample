package com.harman.contacts.ui.about

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.harman.contacts.databinding.AboutFragmentBinding


class AboutFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = AboutFragmentBinding.inflate(inflater)

        setHasOptionsMenu(true)
        return binding.root
    }
}
