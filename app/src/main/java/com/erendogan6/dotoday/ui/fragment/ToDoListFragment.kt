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

@AndroidEntryPoint
class ToDoListFragment : Fragment() {
    private lateinit var binding: FragmentTodoListBinding
    private lateinit var viewmodel: ToDoListViewModel
    private lateinit var adapter: ToDoAdapter
    private var workListID: Int = 0
    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        binding = FragmentTodoListBinding.inflate(layoutInflater, container, false)
        loadToDos()
        setupFloatButton()
        setupSearch()
        setupFilterButton()
        return binding.root
    }

    private fun setupFilterButton() {
        binding.filterIcon.setOnClickListener { view ->
            showFilterPopupMenu(view)
        }
    }

    private fun showFilterPopupMenu(view: View) {
        val popup = PopupMenu(requireContext(), view)
        popup.menuInflater.inflate(R.menu.todo_filter_menu, popup.menu)

        popup.setOnMenuItemClickListener { item ->
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
            popup.menu.findItem(R.id.show_completed).title =
                if (showCompleted) getString(R.string.show_unCompleted) else getString(R.string.show_completed)
        }

        popup.show()
    }


    private fun setupFloatButton() {
        binding.floatButton.setOnClickListener {
            val action = ToDoListFragmentDirections.actionToDoListFragmentToToDoSaveFragment(
                workListID,
                null)
            Navigation.transition(requireView(), action)
        }
    }

    private fun setupSearch() {
        binding.searchIcon.setOnClickListener {
            performSearch()
        }
        binding.searchText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                performSearch()
                true
            } else {
                false
            }
        }
    }

    private fun performSearch() {
        binding.searchText.text.toString().also { searchText ->
            viewmodel.search(searchText, workListID)
        }
    }


    private fun loadToDos() {
        viewmodel.toDoList.observe(viewLifecycleOwner) {
            val toDoList = ArrayList<ToDo>(it)
            adapter = ToDoAdapter(toDoList, onDeleteClicked = { position ->
                val todoItem = toDoList[position]
                Snackbar.make(binding.root,
                              "Do You Want to Delete ${todoItem.title}?",
                              Snackbar.LENGTH_LONG).setAction("Yes") {
                    delete(todoItem)
                    toDoList.removeAt(position)
                    adapter.notifyItemRemoved(position)
                }.show()
            }, onItemClicked = { position ->
                val todoItem = toDoList[position]
                val action = ToDoListFragmentDirections.actionToDoListFragmentToToDoSaveFragment(
                    workListID,
                    todoItem)
                Navigation.transition(requireView(), action)
            }, onCircleClicked = { toDo, itemView ->
                itemView.animate().alpha(0.0f).setDuration(400).withEndAction {
                    toDo.isCompleted = toDo.isCompleted.not()
                    viewmodel.update(toDo, workListID)
                }.start()
            })
            binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
            binding.recyclerView.adapter = adapter
            if (toDoList.size == 0) {
                binding.startText.visibility = View.VISIBLE
            } else {
                binding.startText.visibility = View.INVISIBLE
            }
        }
    }

    private fun delete(toDo: ToDo) {
        viewmodel.delete(toDo, workListID)
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