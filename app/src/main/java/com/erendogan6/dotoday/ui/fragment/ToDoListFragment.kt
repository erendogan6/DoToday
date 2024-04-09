package com.erendogan6.dotoday.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.erendogan6.dotoday.R
import com.erendogan6.dotoday.data.model.ToDo
import com.erendogan6.dotoday.databinding.FragmentTodoListBinding
import com.erendogan6.dotoday.ui.fragment.adaptor.ToDoAdapter
import com.erendogan6.dotoday.ui.fragment.viewmodel.ToDoListViewModel
import com.erendogan6.dotoday.utils.transition
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint class ToDoListFragment : Fragment() {
    private lateinit var binding: FragmentTodoListBinding
    private lateinit var viewmodel: ToDoListViewModel
    private lateinit var adapter: ToDoAdapter
    private var workListID: Int = 0
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentTodoListBinding.inflate(layoutInflater, container, false).apply {
            setupUI()
        }
        loadToDos()
        setupRecyclerView()
        return binding.root
    }

    private fun FragmentTodoListBinding.setupUI() {
        setupFloatButton()
        setupSearch()
        setupFilterButton()
    }

    private fun FragmentTodoListBinding.setupFilterButton() {
        filterIcon.setOnClickListener { view ->
            showFilterPopupMenu(view)
        }
    }

    private fun showFilterPopupMenu(view: View) {
        PopupMenu(requireContext(), view).apply {
            menuInflater.inflate(R.menu.todo_filter_menu, menu)
            setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.show_completed -> {
                        viewmodel.toggleCompletedTasks(workListID)
                        true
                    }

                    R.id.sort_by_due_date -> {
                        viewmodel.sortByDueDate()
                        true
                    }

                    R.id.sort_by_title -> {
                        viewmodel.sortAlphabetically()
                        true
                    }

                    else -> false
                }
            }
            viewmodel.showCompleted.observe(viewLifecycleOwner) { showCompleted ->
                menu.findItem(R.id.show_completed).title = getString(if (showCompleted) R.string.show_unCompleted else R.string.show_completed)
            }
            show()
        }
    }

    private fun FragmentTodoListBinding.setupFloatButton() {
        floatButton.setOnClickListener {
            val action = ToDoListFragmentDirections.actionToDoListFragmentToToDoSaveFragment(workListID, null)
            Navigation.transition(requireView(), action)
        }
    }

    private fun FragmentTodoListBinding.setupSearch() {
        searchIcon.setOnClickListener {
            performSearch()
        }
        searchText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                performSearch()
                true
            } else {
                false
            }
        }
    }

    private fun FragmentTodoListBinding.performSearch() {
        searchText.text.toString().also { searchText ->
            viewmodel.search(searchText, workListID)
        }
    }

    private fun setupRecyclerView() {
        val mutableToDoList = arrayListOf<ToDo>()
        adapter = ToDoAdapter(mutableToDoList, ::onDeleteClicked, ::onItemClicked, ::onCircleClicked)
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = this@ToDoListFragment.adapter
        }
    }

    private fun loadToDos() {
        viewmodel.toDoList.observe(viewLifecycleOwner) { toDoList ->
            adapter.updateData(toDoList)
            binding.startText.visibility = if (toDoList.isEmpty()) View.VISIBLE else View.INVISIBLE
        }
    }

    private fun onDeleteClicked(toDo: ToDo) {
        Snackbar.make(binding.root, "Do You Want to Delete ${toDo.title}?", Snackbar.LENGTH_LONG).setAction("Yes") {
            viewmodel.delete(toDo, workListID)
        }.show()
    }

    private fun onItemClicked(todoItem: ToDo) = navigateToSaveFragment(todoItem)

    private fun onCircleClicked(toDo: ToDo, itemView: View) = itemView.animate().alpha(0.1f).setDuration(300).withEndAction {
        viewmodel.update(toDo.apply { isCompleted = !isCompleted }, workListID)
        itemView.alpha = 1f
    }.start()

    private fun navigateToSaveFragment(toDoItem: ToDo?) {
        val action = ToDoListFragmentDirections.actionToDoListFragmentToToDoSaveFragment(workListID, toDoItem)
        Navigation.transition(requireView(), action)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val args: ToDoListFragmentArgs by navArgs()
        workListID = args.WorkListID
        val tempViewModel: ToDoListViewModel by viewModels()
        viewmodel = tempViewModel
    }

    override fun onResume() {
        super.onResume()
        viewmodel.filterCompletedToDos(workListID)
    }
}