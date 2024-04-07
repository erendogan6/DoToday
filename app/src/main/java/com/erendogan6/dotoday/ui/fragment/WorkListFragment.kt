package com.erendogan6.dotoday.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.erendogan6.dotoday.data.model.WorkList
import com.erendogan6.dotoday.databinding.FragmentWorkListBinding
import com.erendogan6.dotoday.ui.fragment.adaptor.WorkListAdapter
import com.erendogan6.dotoday.ui.fragment.viewmodel.WorkListViewModel
import com.erendogan6.dotoday.utils.transition
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WorkListFragment : Fragment() {
    private lateinit var binding: FragmentWorkListBinding
    private lateinit var viewmodel: WorkListViewModel
    private lateinit var adapter: WorkListAdapter
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentWorkListBinding.inflate(layoutInflater, container, false)
        setupFloatButton()
        setupToolbar()
        setupRecyclerView()
        return binding.root
    }

    fun setupToolbar() {
        binding.textDate.setText("May 5,2023")
    }

    fun setupFloatButton() {
        binding.floatButton.setOnClickListener {
            val dialogFragment = WorkListSaveFragment()
            dialogFragment.show(getParentFragmentManager(), "WorkListSaveFragment")
        }
    }

    fun setupRecyclerView() {
        adapter = WorkListAdapter(
            onDeleteClicked = { workListItem ->
                delete(workListItem)
            },
            onItemClicked = { workListItem ->
                val action =
                    WorkListFragmentDirections.actionWorkListFragmentToToDoListFragment(workListItem.id)
                Navigation.transition(requireView(), action)
            },
            onEditClicked = { workList ->
                val workListUpdateFragment = WorkListUpdateFragment().apply {
                    arguments = Bundle().apply {
                        putSerializable("workList", workList)
                    }
                }
                workListUpdateFragment.show(getParentFragmentManager(), "WorkListUpdateFragment")
            }

        )
        binding.recyclerViewWorkLists.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewWorkLists.adapter = adapter
    }


    private fun observeLiveData() {
        viewmodel.workLists.observe(viewLifecycleOwner) { workLists ->
            workLists?.let {
                adapter.submitList(ArrayList(workLists))
            }
        }
    }

    fun delete(workList: WorkList) {
        viewmodel.deleteWorkList(workList)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setFragmentResultListener("workListUpdate") { _, bundle ->
            val update = bundle.getBoolean("update")
            if (update) {
                viewmodel.getWorkList()
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val tempViewModel: WorkListViewModel by viewModels()
        viewmodel = tempViewModel
        viewmodel.getWorkList()
        observeLiveData()
    }
}