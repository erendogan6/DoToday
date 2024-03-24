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
import com.erendogan6.dotoday.databinding.FragmentMainBinding
import com.erendogan6.dotoday.ui.fragment.adaptor.ToDoAdapter
import com.erendogan6.dotoday.ui.fragment.viewmodel.MainViewModel
import com.erendogan6.dotoday.utils.transition
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainFragment : Fragment() {
    private lateinit var binding: FragmentMainBinding
    private lateinit var viewmodel : MainViewModel
    private lateinit var adapter: ToDoAdapter
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentMainBinding.inflate(layoutInflater,container,false)

        binding.floatButton.setOnClickListener {
            Navigation.transition(requireView(),R.id.action_mainFragment_to_saveFragment)
        }

        binding.search.setOnQueryTextListener(object:SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }

        })

        viewmodel.toDoList.observe(viewLifecycleOwner){
            val ToDoList = ArrayList<ToDo>(it)
            adapter = ToDoAdapter(ToDoList,
                onDeleteClicked = { position ->
                    val todoItem = ToDoList[position]
                    Snackbar.make(binding.root, "Do You Want to Delete ${todoItem.title}?", Snackbar.LENGTH_LONG)
                        .setAction("Yes") {
                            delete(todoItem)
                            ToDoList.removeAt(position)
                            adapter.notifyItemRemoved(position)
                            adapter.notifyDataSetChanged()
                        }.show()
                },
                onItemClicked = { position ->
                    val todoItem = ToDoList[position]
                    val action = MainFragmentDirections.actionMainFragmentToUpdateFragment(todoItem)
                    Navigation.transition(requireView(),action)
                }
            )
            binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
            binding.recyclerView.adapter = adapter
        }

        binding.search.setOnQueryTextListener(object:SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null) {
                    viewmodel.search(query)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }

        })


        return binding.root
    }

    fun delete(toDo: ToDo){
        viewmodel.delete(toDo)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val tempViewModel: MainViewModel by viewModels()
        viewmodel = tempViewModel
    }

    override fun onResume() {
        super.onResume()
        viewmodel.loadToDos()
    }

}