package com.erendogan6.dotoday.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.erendogan6.dotoday.R
import com.erendogan6.dotoday.data.entity.ToDo
import com.erendogan6.dotoday.ui.fragment.adaptor.ToDoAdapter
import com.erendogan6.dotoday.databinding.FragmentMainBinding
import com.erendogan6.dotoday.ui.fragment.viewmodel.MainViewModel
import com.google.android.material.snackbar.Snackbar

class MainFragment : Fragment() {
    private lateinit var binding: FragmentMainBinding
    private lateinit var viewmodel : MainViewModel
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentMainBinding.inflate(layoutInflater,container,false)

        binding.floatButton.setOnClickListener {
            Navigation.findNavController(it).navigate(R.id.action_mainFragment_to_saveFragment)
        }

        binding.search.setOnQueryTextListener(object:SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }

        })

        val toDoList = ArrayList<ToDo>()
        var todo1 = ToDo(0,"Spor","Antrenman",false,1L)
        var todo2 = ToDo(0,"Ders","Antrenman",false,1L)
        var todo3 = ToDo(0,"Alışveriş","Antrenman",false,1L)

        toDoList.add(todo1)
        toDoList.add(todo2)
        toDoList.add(todo3)

        val adapter = ToDoAdapter(toDoList,
            onDeleteClicked = { position ->
                val todoItem = toDoList[position]
                Snackbar.make(binding.root, "Do You Want to Delete ${todoItem.title}?", Snackbar.LENGTH_LONG)
                    .setAction("Yes") {
                        toDoList.removeAt(position)
                    }.show()
            },
            onItemClicked = { position ->
                val todoItem = toDoList[position]
                val action = MainFragmentDirections.actionMainFragmentToUpdateFragment(todoItem)
                Navigation.findNavController(requireView()).navigate(action)
            }
        )

        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = adapter


        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val tempViewModel: MainViewModel by viewModels()
        viewmodel = tempViewModel
    }

}