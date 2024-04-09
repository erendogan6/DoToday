package com.erendogan6.dotoday.ui.fragment

import android.os.Build
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

@AndroidEntryPoint class WorkListUpdateFragment : DialogFragment() {
    private var _binding: FragmentWorklistSaveBinding? = null
    private val binding get() = _binding!!
    private val viewmodel: WorkListViewModel by viewModels()
    private lateinit var workList: WorkList
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentWorklistSaveBinding.inflate(inflater, container, false).apply {
            setupUI()
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        workList = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requireArguments().getSerializable("workList", WorkList::class.java)!!
        } else {
            (arguments?.getSerializable("workList") as? WorkList)!!
        }
        super.onViewCreated(view, savedInstanceState)
    }

    private fun FragmentWorklistSaveBinding.setupUI() {
        saveToolbar.title = "Update Work List"
        editTextName.setText(workList.name)
        deleteIcon.setOnClickListener {
            dismiss()
        }
        setupSaveButton()
    }

    private fun FragmentWorklistSaveBinding.setupSaveButton() {
        buttonSave.setOnClickListener {
            val name = editTextName.text.toString()
            if (name.isBlank()) {
                editTextName.error = "Title Cannot be empty"
                return@setOnClickListener
            }
            update(workList.apply { this.name = name })
        }
    }

    private fun update(workList: WorkList) {
        viewmodel.update(workList) {
            setFragmentResult()
        }
    }

    private fun setFragmentResult() {
        setFragmentResult("workListUpdate", bundleOf("update" to true))
        dismiss()
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.apply {
            setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            setGravity(Gravity.BOTTOM)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}