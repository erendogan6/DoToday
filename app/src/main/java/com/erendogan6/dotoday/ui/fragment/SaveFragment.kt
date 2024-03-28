package com.erendogan6.dotoday.ui.fragment

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import com.erendogan6.dotoday.data.model.WorkList
import com.erendogan6.dotoday.databinding.FragmentWorklistSaveBinding
import com.erendogan6.dotoday.ui.fragment.viewmodel.WorkListViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SaveFragment : DialogFragment() {
    private lateinit var binding: FragmentWorklistSaveBinding
    private lateinit var viewmodel: WorkListViewModel
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentWorklistSaveBinding.inflate(layoutInflater, container, false)
        setupSaveButton()
        setupDeleteButton()
        return binding.root
    }

    fun setupSaveButton() {
        binding.buttonSave.setOnClickListener {
            val listText = binding.editTextName.text.toString()
            if (listText.isNotBlank()) {
                val newList = WorkList(0, listText)
                viewmodel.saveWorkList(newList) {
                    setFragmentResult("workListUpdate", bundleOf("update" to true))
                    dismiss()
                }
            }
        }
    }

    fun setupDeleteButton() {
        binding.deleteIcon.setOnClickListener {
            dismiss()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val tempViewModel: WorkListViewModel by viewModels()
        viewmodel = tempViewModel
    }

    override fun onStart() {
        super.onStart()
        if (dialog != null) {
            val params = dialog!!.window!!.attributes
            params.gravity = Gravity.BOTTOM
            params.width = ViewGroup.LayoutParams.MATCH_PARENT
            params.height = ViewGroup.LayoutParams.WRAP_CONTENT
            dialog!!.window!!.setAttributes(params)
        }
    }
}