package com.erendogan6.dotoday.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.erendogan6.dotoday.data.model.ToDo
import com.erendogan6.dotoday.databinding.FragmentMainBinding
import com.erendogan6.dotoday.ui.fragment.adaptor.ToDoAdapter
import com.erendogan6.dotoday.ui.fragment.viewmodel.MainViewModel
import com.erendogan6.dotoday.utils.transition
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainFragment : Fragment() {
    private lateinit var binding: FragmentMainBinding
    private lateinit var viewmodel: MainViewModel
    private lateinit var adapter: ToDoAdapter
    private var workListID: Int = 0
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMainBinding.inflate(layoutInflater, container, false)
        loadToDos()
        setupFloatButton()
        setupSearch()
        return binding.root
    }

    private fun setupFloatButton() {
        binding.floatButton.setOnClickListener {
            val action = MainFragmentDirections.actionMainFragmentToSaveFragment(workListID)
            Navigation.transition(requireView(), action)
        }
    }

    private fun setupSearch() {
        binding.searchIcon.setOnClickListener {
            val searchText = binding.searchText.text.toString()
            if (searchText.isNotBlank()) {
                viewmodel.search(searchText)
            }
        }
    }

    private fun loadToDos() {
        viewmodel.toDoList.observe(viewLifecycleOwner) {
            val toDoList = ArrayList<ToDo>(it)
            adapter = ToDoAdapter(toDoList,
                onDeleteClicked = { position ->
                    val todoItem = toDoList[position]
                    Snackbar.make(
                        binding.root,
                        "Do You Want to Delete ${todoItem.title}?",
                        Snackbar.LENGTH_LONG
                    )
                        .setAction("Yes") {
                            delete(todoItem)
                            toDoList.removeAt(position)
                            adapter.notifyItemRemoved(position)
                            adapter.notifyDataSetChanged()
                        }.show()
                },
                onItemClicked = { position ->
                    val todoItem = toDoList[position]
                    val action = MainFragmentDirections.actionMainFragmentToUpdateFragment(
                        todoItem,
                        workListID
                    )
                    Navigation.transition(requireView(), action)
                }
            )
            binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
            binding.recyclerView.adapter = adapter
        }
    }

    private fun delete(toDo: ToDo) {
        viewmodel.delete(toDo, workListID)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val args: MainFragmentArgs by navArgs()
        workListID = args.WorkListID
        val tempViewModel: MainViewModel by viewModels()
        viewmodel = tempViewModel
    }

    override fun onResume() {
        super.onResume()
        viewmodel.loadToDos(workListID)
    }
}