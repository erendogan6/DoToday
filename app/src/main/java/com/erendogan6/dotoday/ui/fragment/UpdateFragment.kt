package com.erendogan6.dotoday.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.erendogan6.dotoday.R
import com.erendogan6.dotoday.databinding.FragmentUpdateBinding
import com.erendogan6.dotoday.ui.fragment.viewmodel.MainViewModel
import com.erendogan6.dotoday.ui.fragment.viewmodel.UpdateViewModel
import com.erendogan6.dotoday.utils.transition
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UpdateFragment : Fragment() {
    private lateinit var binding: FragmentUpdateBinding
    private lateinit var viewmodel : UpdateViewModel
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentUpdateBinding.inflate(inflater,container,false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val args: UpdateFragmentArgs by navArgs()
        val toDo = args.ToDo
        binding.editTextName.setText(toDo.title)
        super.onViewCreated(view, savedInstanceState)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val tempViewModel: UpdateViewModel by viewModels()
        viewmodel = tempViewModel
    }
}