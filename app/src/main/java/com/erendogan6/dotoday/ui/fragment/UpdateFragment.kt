package com.erendogan6.dotoday.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.erendogan6.dotoday.R
import com.erendogan6.dotoday.databinding.FragmentUpdateBinding


class UpdateFragment : Fragment() {
    private lateinit var binding: FragmentUpdateBinding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentUpdateBinding.inflate(inflater,container,false)
        return binding.root
    }

}