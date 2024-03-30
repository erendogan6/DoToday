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

@AndroidEntryPoint
class WorkListUpdateFragment : DialogFragment() {
    private lateinit var binding: FragmentWorklistSaveBinding
    private lateinit var viewmodel: WorkListViewModel
    private lateinit var workList: WorkList
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentWorklistSaveBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        workList = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requireArguments().getSerializable("workList", WorkList::class.java)!!
        } else {
            (arguments?.getSerializable("workList") as? WorkList)!!
        }
        binding.saveToolbar.title = "Update Work List"
        binding.editTextName.setText(workList.name)
        binding.deleteIcon.setOnClickListener {
            dismiss()
        }
        setupSaveButton()
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val tempViewModel: WorkListViewModel by viewModels()
        viewmodel = tempViewModel
    }

    private fun setupSaveButton() {
        binding.buttonSave.setOnClickListener {
            val name = binding.editTextName.text.toString()
            if (name.isBlank()) {
                binding.editTextName.error = "Title Cannot be empty"
                return@setOnClickListener
            }
            workList.name = name
            viewmodel.update(workList) {
                setFragmentResult("workListUpdate", bundleOf("update" to true))
                dismiss()
            }
        }
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