package com.erendogan6.dotoday.ui.fragment.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.erendogan6.dotoday.data.model.ToDo
import com.erendogan6.dotoday.data.repository.DoTodayRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ToDoListViewModel @Inject constructor(var repo: DoTodayRepository) : ViewModel() {
    var toDoList = MutableLiveData<List<ToDo>>()

    fun delete(toDo: ToDo, id: Int) {
        CoroutineScope(Dispatchers.Main).launch {
            repo.delete(toDo)
            loadToDos(id)
        }
    }

    fun loadToDos(id: Int) {
        CoroutineScope(Dispatchers.Main).launch {
            toDoList.value = repo.getToDosForWorkList(id)
        }
    }

    fun search(searchText: String) {
        CoroutineScope(Dispatchers.Main).launch {
            toDoList.value = repo.search(searchText)
        }
    }
}