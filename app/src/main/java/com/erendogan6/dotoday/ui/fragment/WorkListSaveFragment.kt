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

@AndroidEntryPoint class WorkListSaveFragment : DialogFragment() {
    private var _binding: FragmentWorklistSaveBinding? = null
    private val binding get() = _binding!!
    private val viewmodel: WorkListViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWorklistSaveBinding.inflate(layoutInflater, container, false).apply {
            setupSaveButton()
            setupDeleteButton()
        }
        return binding.root
    }

    private fun FragmentWorklistSaveBinding.setupSaveButton() {
        buttonSave.setOnClickListener {
            editTextName.text.toString().takeIf { it.isNotBlank() }?.let { listText ->
                val newList = WorkList(0, listText)
                saveWorkList(newList)
            }
        }
    }

    private fun saveWorkList(workList: WorkList) {
        viewmodel.saveWorkList(workList) {
            setFragmentResult()
        }
    }

    private fun setFragmentResult() {
        setFragmentResult("workListUpdate", bundleOf("update" to true))
        dismiss()
    }

    private fun FragmentWorklistSaveBinding.setupDeleteButton() = deleteIcon.setOnClickListener {
        dismiss()
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.apply {
            setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            setGravity(Gravity.BOTTOM)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}