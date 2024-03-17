package com.erendogan6.dotoday.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.erendogan6.dotoday.R
import com.erendogan6.dotoday.databinding.FragmentMainBinding
import com.erendogan6.dotoday.databinding.FragmentSaveBinding

class SaveFragment : Fragment() {
    private lateinit var binding: FragmentSaveBinding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentSaveBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

}