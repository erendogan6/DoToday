package com.erendogan6.dotoday.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.erendogan6.dotoday.data.model.ToDo
import com.erendogan6.dotoday.databinding.FragmentEisenHowerListBinding
import com.erendogan6.dotoday.ui.fragment.adaptor.ToDoAdapter
import com.erendogan6.dotoday.ui.fragment.viewmodel.EisenHowerListViewModel
import com.erendogan6.dotoday.utils.transition
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint class EisenHowerList : Fragment() {
    private var _binding: FragmentEisenHowerListBinding? = null
    private val binding get() = _binding!!
    private val viewModel: EisenHowerListViewModel by viewModels()
    private lateinit var adapter: ToDoAdapter
    private val args: EisenHowerListArgs by navArgs()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentEisenHowerListBinding.inflate(layoutInflater, container, false).apply {
            setupUI()
        }
        getToDos()
        observeToDos()
        return binding.root
    }

    private fun FragmentEisenHowerListBinding.setupUI() {
        setupRecyclerView()
    }

    private fun FragmentEisenHowerListBinding.setupRecyclerView() {
        adapter = ToDoAdapter(arrayListOf(), ::onDeleteClicked, ::onItemClicked, ::onCircleClicked)
        recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = this@EisenHowerList.adapter
        }
    }

    private fun getToDos() {
        viewModel.dataLoaded.observe(viewLifecycleOwner) { loaded ->
            if (loaded) {
                viewModel.filterTodosForCategory(args.categoryName)
            }
        }
    }

    private fun observeToDos() {
        viewModel.todos.observe(viewLifecycleOwner) { toDoList ->
            if (toDoList != null) {
                adapter.updateData(toDoList)
                binding.startText.visibility = if (toDoList.isEmpty()) View.VISIBLE else View.INVISIBLE
            }
        }
    }

    private fun onDeleteClicked(toDo: ToDo) {
        Snackbar.make(binding.root, "Do You Want to Delete ${toDo.title}?", Snackbar.LENGTH_LONG).setAction("Yes") {
            viewModel.deleteToDoAndUpdateView(toDo)
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
        viewModel.updateToDoAndUpdateView(toDo.apply { isCompleted = !isCompleted })
        itemView.alpha = 1f
    }.start()

    private fun navigateToSaveFragment(toDoItem: ToDo?) {
        val action = ToDoListFragmentDirections.actionToDoListFragmentToToDoSaveFragment(toDoItem!!.workListId, toDoItem)
        Navigation.transition(requireView(), action)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}