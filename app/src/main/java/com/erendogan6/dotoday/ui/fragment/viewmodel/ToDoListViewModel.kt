package com.erendogan6.dotoday.ui.fragment.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.erendogan6.dotoday.data.model.ToDo
import com.erendogan6.dotoday.data.repository.DoTodayRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel class ToDoListViewModel @Inject constructor(private var repo: DoTodayRepository) : ViewModel() {
    private val _toDoList = MutableLiveData<List<ToDo>>()
    val toDoList: LiveData<List<ToDo>> = _toDoList

    private val _showCompleted = MutableLiveData(false)
    val showCompleted: LiveData<Boolean> = _showCompleted

    fun toggleCompletedTasksToDo(workListId: Int) {
        _showCompleted.value = !(_showCompleted.value ?: false)
        filterCompletedToDos(workListId)
    }

    fun deleteToDo(toDo: ToDo, id: Int) = viewModelScope.launch {
        repo.deleteToDo(toDo)
        filterCompletedToDos(id)
    }

    fun searchToDo(searchText: String, workListId: Int) = viewModelScope.launch {
        val searchResults = if (searchText.isBlank()) {
            repo.getToDosForWorkList(workListId)
        } else {
            repo.searchToDo(searchText, workListId)
        }
        _toDoList.postValue(searchResults)
    }


    fun saveToDo(toDo: ToDo, id: Int) = viewModelScope.launch {
        repo.saveToDo(toDo)
        filterCompletedToDos(id)
    }

    fun updateToDo(toDo: ToDo, id: Int) = viewModelScope.launch {
        repo.updateToDo(toDo)
        filterCompletedToDos(id)
    }

    fun filterCompletedToDos(workListId: Int) = viewModelScope.launch {
        val isCompleted = _showCompleted.value ?: false
        val filteredList = withContext(Dispatchers.IO) {
            if (isCompleted) repo.getCompletedTodos(workListId)
            else repo.getNonCompletedTodos(workListId)
        }
        _toDoList.postValue(filteredList.sortedBy { it.dueDate })
    }

    fun sortToDoByPriority() {
        val priorityOrder = mapOf("high" to 3, "medium" to 2, "low" to 1)
        _toDoList.value = toDoList.value?.sortedByDescending { priorityOrder[it.priority] ?: 0 }
    }


    fun sortToDoByDueDate() {
        _toDoList.value = toDoList.value?.sortedBy { it.dueDate }
    }

    fun sortToDoAlphabetically() {
        _toDoList.value = toDoList.value?.sortedWith(compareBy(String.CASE_INSENSITIVE_ORDER) { it.title })
    }
}