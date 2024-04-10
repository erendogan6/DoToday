package com.erendogan6.dotoday.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.PopupMenu
import android.widget.Toast
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
    private var _binding: FragmentTodoListBinding? = null
    private val binding get() = _binding!!
    private val viewmodel: ToDoListViewModel by viewModels()
    private lateinit var adapter: ToDoAdapter
    private var workListID: Int = 0
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentTodoListBinding.inflate(layoutInflater, container, false).apply {
            setupUI()
        }
        loadToDos()
        return binding.root
    }

    private fun FragmentTodoListBinding.setupUI() {
        setupFloatButton()
        setupSearch()
        setupFilterButton()
        setupRecyclerView()
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
                        viewmodel.toggleCompletedTasksToDo(workListID)
                        true
                    }

                    R.id.sort_by_due_date -> {
                        viewmodel.sortToDoByDueDate()
                        true
                    }

                    R.id.sort_by_title -> {
                        viewmodel.sortToDoAlphabetically()
                        true
                    }

                    R.id.sort_by_priority -> {
                        viewmodel.sortToDoByPriority()
                        true
                    }

                    else -> false
                }
            }
            observeCompletedFilter(this)
            show()
        }
    }

    private fun observeCompletedFilter(popMenu: PopupMenu) {
        viewmodel.showCompleted.observe(viewLifecycleOwner) { showCompleted ->
            popMenu.menu.findItem(R.id.show_completed).title = getString(if (showCompleted) R.string.show_unCompleted else R.string.show_completed)
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

    private fun performSearch() {
        viewmodel.searchToDo(binding.searchText.text.toString(), workListID)
    }

    private fun FragmentTodoListBinding.setupRecyclerView() {
        adapter = ToDoAdapter(arrayListOf(), ::onDeleteClicked, ::onItemClicked, ::onCircleClicked)
        recyclerView.apply {
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
            viewmodel.deleteToDo(toDo, workListID)
        }.show()
    }

    private fun onItemClicked(todoItem: ToDo) = navigateToSaveFragment(todoItem)

    private fun onCircleClicked(toDo: ToDo, itemView: View) = itemView.animate().alpha(0.1f).setDuration(300).withEndAction {
        val message = if (toDo.isCompleted) {
            "ToDo Uncompleted"
        } else {
            "ToDo Completed"
        }
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        viewmodel.updateToDo(toDo.apply { isCompleted = !isCompleted }, workListID)
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
    }

    override fun onResume() {
        super.onResume()
        viewmodel.filterCompletedToDos(workListID)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}