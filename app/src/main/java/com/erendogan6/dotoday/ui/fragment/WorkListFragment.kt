package com.erendogan6.dotoday.ui.fragment

import android.icu.text.SimpleDateFormat
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.erendogan6.dotoday.data.model.WorkList
import com.erendogan6.dotoday.databinding.FragmentWorkListBinding
import com.erendogan6.dotoday.ui.fragment.adaptor.WorkListAdapter
import com.erendogan6.dotoday.ui.fragment.viewmodel.WorkListViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import java.util.Locale

@AndroidEntryPoint class WorkListFragment : Fragment() {
    private var _binding: FragmentWorkListBinding? = null
    private val binding get() = _binding!!
    private val viewModel: WorkListViewModel by viewModels()
    private val adapter: WorkListAdapter by lazy {
        WorkListAdapter(
            onDeleteClicked = this::deleteWorklist,
            onItemClicked = this::navigateToToDoList,
            onEditClicked = this::showWorkListUpdateFragment,
            calculateCompletionPercentage = this::calculateProgess
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWorkListBinding.inflate(layoutInflater, container, false).apply {
            setupUI()
        }
        return binding.root
    }

    private fun FragmentWorkListBinding.setupUI() {
        setupMatrisButton()
        setupFloatButton()
        updateDateInView()
        setupRecyclerView()
    }

    private fun FragmentWorkListBinding.setupMatrisButton() {
        matrisButton.setOnClickListener {
            val action = WorkListFragmentDirections.actionWorkListFragmentToEisenHowerMatrixFragment()
            findNavController().navigate(action)
        }
    }

    private fun FragmentWorkListBinding.updateDateInView() {
        val dateFormat = SimpleDateFormat("EEEE, d MMMM", Locale.getDefault())
        textDate.text = dateFormat.format(System.currentTimeMillis())
    }

    private fun FragmentWorkListBinding.setupFloatButton() {
        floatButton.setOnClickListener {
            val dialogFragment = WorkListSaveFragment()
            dialogFragment.show(getParentFragmentManager(), "WorkListSaveFragment")
        }
    }

    private fun FragmentWorkListBinding.setupRecyclerView() {
        recyclerViewWorkLists.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = this@WorkListFragment.adapter
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getWorkList()
        observeWorkLists()
        setupFragmentResultListener()
    }

    private fun navigateToToDoList(workListItemId: Int) {
        val action = WorkListFragmentDirections.actionWorkListFragmentToToDoListFragment(workListItemId)
        findNavController().navigate(action)
    }

    private fun showWorkListUpdateFragment(workList: WorkList) {
        val args = bundleOf("workList" to workList)
        WorkListUpdateFragment().apply { arguments = args }.show(parentFragmentManager, "WorkListUpdateFragment")
    }

    private fun observeWorkLists() {
        viewModel.workLists.observe(viewLifecycleOwner) { workLists ->
            adapter.submitList(ArrayList(workLists))
            binding.startText.visibility = if (workLists.isNullOrEmpty()) View.VISIBLE else View.INVISIBLE
        }
    }

    private fun setupFragmentResultListener() {
        parentFragmentManager.setFragmentResultListener("workListUpdate", viewLifecycleOwner) { _, bundle ->
            if (bundle.getBoolean("update", false)) {
                viewModel.getWorkList()
            }
        }
    }

    private fun calculateProgess(workListId: Int, callback: (Int) -> Unit) {
        viewModel.calculateCompletionPercentage(workListId).observe(viewLifecycleOwner) { percentage ->
            callback(percentage)
        }
    }

    private fun deleteWorklist(workList: WorkList) {
        Snackbar.make(requireContext(), requireView(), "Are You Want Delete ${workList.name}", Snackbar.LENGTH_SHORT).setAction("Yes") {
            viewModel.deleteWorkList(workList)
        }.show()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}