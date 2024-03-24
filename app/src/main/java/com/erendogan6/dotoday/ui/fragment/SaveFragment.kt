package com.erendogan6.dotoday.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.erendogan6.dotoday.R
import com.erendogan6.dotoday.databinding.FragmentMainBinding
import com.erendogan6.dotoday.databinding.FragmentSaveBinding
import com.erendogan6.dotoday.ui.fragment.viewmodel.MainViewModel
import com.erendogan6.dotoday.ui.fragment.viewmodel.SaveViewModel
import com.erendogan6.dotoday.utils.transition
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SaveFragment : Fragment() {
    private lateinit var binding: FragmentSaveBinding
    private lateinit var viewmodel : SaveViewModel
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentSaveBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val tempViewModel: SaveViewModel by viewModels()
        viewmodel = tempViewModel
    }

}