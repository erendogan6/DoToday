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
class ToDoListViewModel @Inject constructor(private var repo: DoTodayRepository) : ViewModel() {
    var toDoList = MutableLiveData<List<ToDo>>()
    var showCompleted = MutableLiveData(false)

    fun toggleCompletedTasks(workListId: Int) {
        showCompleted.value = !(showCompleted.value)!!
        filterCompletedToDos(workListId)
    }

    fun delete(toDo: ToDo, id: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            repo.delete(toDo)
            filterCompletedToDos(id)
        }
    }

    fun loadToDos(id: Int) {
        CoroutineScope(Dispatchers.Main).launch {
            toDoList.value = repo.getToDosForWorkList(id)
        }
    }

    fun search(searchText: String) {
        CoroutineScope(Dispatchers.IO).launch {
            toDoList.value = repo.search(searchText)
        }
    }

    fun save(toDo: ToDo, id: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            repo.save(toDo)
            filterCompletedToDos(id)
        }
    }

    fun update(toDo: ToDo, id: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            repo.update(toDo)
            loadToDos(id)
        }
    }
            filterCompletedToDos(id)
        }
    }

    fun filterCompletedToDos(workListId: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            val filteredList = if (showCompleted.value!!) {
                repo.getCompletedTodos(workListId)
            } else {
                repo.getNonCompletedTodos(workListId)
            }
            toDoList.postValue(filteredList.sortedBy { it.dueDate })
        }
    }

    fun sortByDueDate() {
        toDoList.value = toDoList.value?.sortedBy { it.dueDate }
    }

    fun sortAlphabetically() {
        toDoList.value =
            toDoList.value?.sortedWith(compareBy(String.CASE_INSENSITIVE_ORDER) { it.title })
    }


}